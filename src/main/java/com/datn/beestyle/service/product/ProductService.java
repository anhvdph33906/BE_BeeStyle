package com.datn.beestyle.service.product;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.product.CreateProductRequest;
import com.datn.beestyle.dto.product.ProductResponse;
import com.datn.beestyle.dto.product.UpdateProductRequest;
import com.datn.beestyle.dto.product.variant.CreateProductVariantRequest;
import com.datn.beestyle.entity.product.Product;
import com.datn.beestyle.entity.product.ProductImage;
import com.datn.beestyle.entity.product.ProductVariant;
import com.datn.beestyle.entity.product.attributes.Color;
import com.datn.beestyle.entity.product.attributes.Size;
import com.datn.beestyle.enums.GenderProduct;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.InvalidDataException;
import com.datn.beestyle.mapper.ProductMapper;
import com.datn.beestyle.mapper.ProductVariantMapper;
import com.datn.beestyle.repository.ColorRepository;
import com.datn.beestyle.repository.product.ProductRepository;
import com.datn.beestyle.repository.SizeRepository;
import com.datn.beestyle.service.category.ICategoryService;
import com.datn.beestyle.service.brand.IBrandService;
import com.datn.beestyle.service.material.IMaterialService;
import com.datn.beestyle.util.AppUtils;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService
        extends GenericServiceAbstract<Product, Long, CreateProductRequest, UpdateProductRequest, ProductResponse>
        implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final IBrandService brandService;
    private final IMaterialService materialService;
    private final ICategoryService categoryService;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductVariantMapper productVariantMapper;

    public ProductService(IGenericRepository<Product, Long> entityRepository,
                          IGenericMapper<Product, CreateProductRequest, UpdateProductRequest, ProductResponse> mapper,
                          EntityManager entityManager, ProductRepository productRepository,
                          ProductMapper productMapper, IBrandService brandService, IMaterialService materialService,
                          ICategoryService categoryService, ColorRepository colorRepository, SizeRepository sizeRepository,
                          ProductVariantMapper productVariantMapper) {
        super(entityRepository, mapper, entityManager);
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.materialService = materialService;
        this.categoryService = categoryService;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productVariantMapper = productVariantMapper;
    }

    @Override
    public PageResponse<List<ProductResponse>> searchProductByStatusIsActive(Pageable pageable, String keyword) {
        int page = 0, pageSize = 20;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;
        if (pageable.getPageSize() > 0) pageSize = pageable.getPageSize();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<ProductResponse> productResponsePages = productRepository.searchProduct(pageRequest, keyword, 1);

        return PageResponse.<List<ProductResponse>>builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageRequest.getPageSize())
                .totalElements(productResponsePages.getTotalElements())
                .totalPages(productResponsePages.getTotalPages())
                .items(productResponsePages.getContent())
                .build();
    }

    @Override
    public PageResponse<List<ProductResponse>> filterProductByStatusIsActive(Pageable pageable, String keyword, String categoryIds, String genderProduct,
                                                                             String brandIds, String materialIds,
                                                                             BigDecimal minPrice, BigDecimal maxPrice) {
        Integer genderProductValue = null;
        if (genderProduct != null) {
            GenderProduct genderProductEnum = GenderProduct.fromString(genderProduct);
            if (genderProductEnum != null) genderProductValue = genderProductEnum.getValue();
        }

        List<Integer> brandIdList = AppUtils.handleStringIdsToIntegerIdList(brandIds);
        List<Integer> materialIdList = AppUtils.handleStringIdsToIntegerIdList(materialIds);
        List<Integer> categoryIdList = AppUtils.handleStringIdsToIntegerIdList(categoryIds);

        Page<ProductResponse> productResponsePages =
                productRepository.filterProduct(pageable, keyword, categoryIdList, genderProductValue, brandIdList, materialIdList,
                        minPrice, maxPrice, 1);

        return PageResponse.<List<ProductResponse>>builder()
                .pageNo(productResponsePages.getNumber() + 1)
                .pageSize(productResponsePages.getSize())
                .totalElements(productResponsePages.getTotalElements())
                .totalPages(productResponsePages.getTotalPages())
                .items(productResponsePages.getContent())
                .build();
    }

    @Override
    public PageResponse<List<ProductResponse>> getProductsFilterByFields(Pageable pageable, String keyword,
                                                                         String category, String genderProduct, String brandIds,
                                                                         String materialIds, String status) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;
        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by("createdAt", "id").descending());

        Integer categoryId = null;
        if (category != null) {
            try {
                categoryId = Integer.parseInt(category);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        Integer genderProductValue = null;
        if (genderProduct != null) {
            GenderProduct genderProductEnum = GenderProduct.fromString(genderProduct);
            if (genderProductEnum != null) genderProductValue = genderProductEnum.getValue();
        }

        List<Integer> brandIdList = AppUtils.handleStringIdsToIntegerIdList(brandIds);
        List<Integer> materialIdList = AppUtils.handleStringIdsToIntegerIdList(materialIds);

        Integer statusValue = null;
        if (status != null) {
            Status statusEnum = Status.fromString(status);
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }

        Page<ProductResponse> productResponsePages = productRepository.findAllByFields(pageRequest, keyword, categoryId,
                genderProductValue, brandIdList, materialIdList, statusValue);
        return PageResponse.<List<ProductResponse>>builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(productResponsePages.getTotalElements())
                .totalPages(productResponsePages.getTotalPages())
                .items(productResponsePages.getContent())
                .build();
    }

    @Transactional
    @Override
    public ProductResponse create(CreateProductRequest request) {
        this.beforeCreate(request);
        Product entity = mapper.toCreateEntity(request);
        this.afterConvertCreateRequest(request, entity);
        return productMapper.toEntityDto(productRepository.save(entity));
    }

    @Override
    protected List<CreateProductRequest> beforeCreateEntities(List<CreateProductRequest> requests) {
        return null;
    }

    @Override
    protected List<UpdateProductRequest> beforeUpdateEntities(List<UpdateProductRequest> requests) {
        return null;
    }


    @Override
    protected void beforeCreate(CreateProductRequest request) {
        String productName = request.getProductName().trim();
        if (productRepository.existsByProductName(productName))
            throw new InvalidDataException("Tên sản phẩm đã tồn tại.");
        request.setProductName(productName);

        String productCode = request.getProductCode();
        if (StringUtils.hasText(productCode)) {
            productCode = productCode.trim();
            if (productRepository.existsByProductCode(productCode))
                throw new InvalidDataException("Mã sản phẩm đã tồn tại.");
            request.setProductCode(productCode);
        } else {
            request.setProductCode(null);
        }
    }

    @Override
    protected void beforeUpdate(Long id, UpdateProductRequest request) {
        request.setProductName(request.getProductName().trim());
    }

    @Override
    protected void afterConvertCreateRequest(CreateProductRequest request, Product product) {
        Integer brandId = request.getBrandId();
        if (brandId != null) product.setBrand(brandService.getById(brandId));

        Integer materialId = request.getMaterialId();
        if (materialId != null) product.setMaterial(materialService.getById(materialId));

        Integer categoryId = request.getCategoryId();
        if (categoryId != null) product.setCategory(categoryService.getById(categoryId));

        List<ProductImage> productImages = request.getProductImages();
        if (!productImages.isEmpty()) {
            productImages.forEach(productImage -> {
                if (productImage.getImageUrl() == null || productImage.getImageUrl().trim().isEmpty()) {
                    productImage.setImageUrl("/no-img.png");
                }
                product.addProductImage(productImage);
            });
        }

        List<CreateProductVariantRequest> createProductVariantRequests = request.getProductVariants();
        if (!createProductVariantRequests.isEmpty()) {
            List<Integer> colorIds = createProductVariantRequests.stream().map(CreateProductVariantRequest::getColorId).toList();
            List<Integer> sizeIds = createProductVariantRequests.stream().map(CreateProductVariantRequest::getSizeId).toList();

            List<Color> colors = colorRepository.findAllById(colorIds);
            List<Size> sizes = sizeRepository.findAllById(sizeIds);

            if (colors.isEmpty()) throw new InvalidDataException("Color ids not found: " + colorIds);
            if (sizes.isEmpty()) throw new InvalidDataException("Size ids not found: " + sizeIds);

            Map<Integer, Color> colorMap = colors.stream().collect(Collectors.toMap(Color::getId, color -> color));
            Map<Integer, Size> sizeMap = sizes.stream().collect(Collectors.toMap(Size::getId, size -> size));

            this.validatePropsProductVariants(colorIds, sizeIds, colorMap, sizeMap);

            createProductVariantRequests.forEach(variantRequest -> {
                Color color = colorMap.get(variantRequest.getColorId());
                Size size = sizeMap.get(variantRequest.getSizeId());

                ProductVariant productVariant = productVariantMapper.toCreateEntity(variantRequest);

                productVariant.setColor(color);
                productVariant.setSize(size);

                product.addProductVariant(productVariant);
            });
        }
    }

    @Override
    protected void afterConvertUpdateRequest(UpdateProductRequest request, Product productUpdate) {
        Optional<Product> productByName =
                productRepository.findByProductNameAndIdNot(request.getProductName(), productUpdate.getId());
        if (productByName.isPresent()) throw new InvalidDataException("Tên sản phẩm đã tồn tại.");

        Integer brandId = request.getBrandId();
        if (brandId != null) productUpdate.setBrand(brandService.getById(brandId));

        Integer materialId = request.getMaterialId();
        if (materialId != null) productUpdate.setMaterial(materialService.getById(materialId));

        Integer categoryId = request.getCategoryId();
        if (categoryId != null) productUpdate.setCategory(categoryService.getById(categoryId));
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

    private void validatePropsProductVariants(List<Integer> colorIds, List<Integer> sizeIds,
                                              Map<Integer, Color> colorMap, Map<Integer, Size> sizeMap) {
        List<Integer> invalidColorIds = colorIds.stream().filter(id -> colorMap.get(id) == null).toList();
        List<Integer> invalidSizeIds = sizeIds.stream().filter(id -> sizeMap.get(id) == null).toList();

        if (!invalidColorIds.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Color ID không tồn tại: ");
            for (Integer invalidId : invalidColorIds) {
                errorMessage.append(invalidId).append(", ");
            }
            errorMessage.setLength(errorMessage.length() - 2);
            throw new IllegalArgumentException(errorMessage.toString());
        }

        if (!invalidSizeIds.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Size ID không tồn tại: ");
            for (Integer invalidId : invalidSizeIds) {
                errorMessage.append(invalidId).append(", ");
            }
            errorMessage.setLength(errorMessage.length() - 2);
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }
}
