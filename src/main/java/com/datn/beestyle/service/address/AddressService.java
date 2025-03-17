package com.datn.beestyle.service.address;

import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.address.AddressResponse;
import com.datn.beestyle.dto.address.CreateAddressRequest;
import com.datn.beestyle.dto.address.UpdateAddressRequest;
import com.datn.beestyle.entity.Address;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.exception.ResourceNotFoundException;
import com.datn.beestyle.repository.AddressRepository;
import com.datn.beestyle.repository.customer.CustomerRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class AddressService
        extends GenericServiceAbstract<Address, Long, CreateAddressRequest, UpdateAddressRequest, AddressResponse>
        implements IAddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressService(IGenericRepository<Address, Long> entityRepository, IGenericMapper<Address, CreateAddressRequest, UpdateAddressRequest, AddressResponse> mapper, EntityManager entityManager, AddressRepository addressRepository, CustomerRepository customerRepository) {
        super(entityRepository, mapper, entityManager);
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public PageResponse<?> getAllByCustomerId(Pageable pageable, Long customerId) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;


        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "isDefault"));


        Page<Address> addressPage = addressRepository.findByCustomerId(pageRequest, customerId);
        List<AddressResponse> addressResponseList = mapper.toEntityDtoList(addressPage.getContent());

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(addressPage.getTotalElements())
                .totalPages(addressPage.getTotalPages())
                .items(addressResponseList)
                .build();
    }

    public AddressResponse setUpdateIsDefault(Long id, UpdateAddressRequest request) {
        // Kiểm tra nếu yêu cầu đặt địa chỉ này làm mặc định
        if (request.getIsDefault()) {
            Address currentAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

            // Nếu địa chỉ hiện tại chưa được đặt là mặc định, tiến hành cập nhật
            if (!currentAddress.getIsDefault()) {
                // Đặt các địa chỉ khác của khách hàng này thành không mặc định
                addressRepository.updateIsDefaultFalseForOtherAddresses(currentAddress.getCustomer().getId(), id);

                // Đặt địa chỉ hiện tại thành mặc định
                currentAddress.setIsDefault(true);
                return mapper.toEntityDto(addressRepository.save(currentAddress));  // Lưu và trả về AddressResponse
            }
        } else {
            // Nếu không cần mặc định, chỉ cập nhật `isDefault` thành false
            Address addressToUpdate = addressRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

            addressToUpdate.setIsDefault(false); // Đảm bảo địa chỉ này không là mặc định

            // Thực hiện cập nhật khác từ request (nếu cần thiết)
            // addressToUpdate.setAddressName(request.getAddressName());

            return mapper.toEntityDto(addressRepository.save(addressToUpdate));
        }

        // Nếu không cần cập nhật gì, trả về địa chỉ hiện tại đã ở trạng thái mặc định
        return mapper.toEntityDto(addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id)));
    }

    @Override
    protected List<CreateAddressRequest> beforeCreateEntities(List<CreateAddressRequest> requests) {
        return null;
    }

    @Override
    protected List<UpdateAddressRequest> beforeUpdateEntities(List<UpdateAddressRequest> requests) {
        return null;
    }

    @Override
    protected void beforeCreate(CreateAddressRequest request) {
        log.info("request: {}",request.getCustomerId());
    }

    @Override
    protected void beforeUpdate(Long aLong, UpdateAddressRequest request) {

    }

    @Override
    protected void afterConvertCreateRequest(CreateAddressRequest request, Address entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateAddressRequest request, Address entity) {

    }

    @Override
    protected String getEntityName() {
        return "Address";
    }

    @Override
    public AddressResponse create(CreateAddressRequest request) {
        // Lấy customer từ customerId

        if(addressRepository.countAddressByCustomer(request.getCustomerId())>=3){
            throw new IllegalArgumentException("Mỗi khách hàng chỉ có tối đa 3 địa chỉ");
        }
        // Kiểm tra nếu không có bản ghi nào với isDefault = true
        boolean existsDefaultAddress = addressRepository.existsByCustomerIdAndIsDefaultTrue(request.getCustomerId());

        // Nếu chưa có bản ghi nào có isDefault = true, đặt isDefault của bản ghi mới là true
        request.setIsDefault(!existsDefaultAddress);
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Tạo đối tượng Address từ request
        Address address = mapper.toCreateEntity(request);

        // Gán customer vào address
        address.setCustomer(customer);
        addressRepository.save(address);

        return mapper.toEntityDto(address);
    }
}
