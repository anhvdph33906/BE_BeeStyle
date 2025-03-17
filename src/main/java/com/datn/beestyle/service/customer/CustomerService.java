package com.datn.beestyle.service.customer;


import com.datn.beestyle.common.GenericServiceAbstract;
import com.datn.beestyle.common.IGenericMapper;
import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.dto.PageResponse;
import com.datn.beestyle.dto.customer.*;
import com.datn.beestyle.entity.user.Customer;
import com.datn.beestyle.enums.Gender;
import com.datn.beestyle.enums.Status;
import com.datn.beestyle.exception.DuplicateEmailException;
import com.datn.beestyle.repository.customer.CustomerRepository;
import com.datn.beestyle.repository.StaffRepository;
import com.datn.beestyle.repository.customer.CustomerRepositoryCustom;
import com.datn.beestyle.service.mail.MailService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerService
        extends GenericServiceAbstract<Customer, Long, CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse>
        implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;
    private final StaffRepository staffRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(IGenericRepository<Customer, Long> entityRepository,
                           IGenericMapper<Customer, CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse> mapper,
                           EntityManager entityManager, CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom, StaffRepository staffRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        super(entityRepository, mapper, entityManager);
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.staffRepository = staffRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected List<CreateCustomerRequest> beforeCreateEntities(List<CreateCustomerRequest> requests) {
        return requests;
    }

    @Override
    protected List<UpdateCustomerRequest> beforeUpdateEntities(List<UpdateCustomerRequest> requests) {
        return null;
    }

    @Override
    protected void beforeCreate(CreateCustomerRequest request) {
//        if (request.getPassword() == null || request.getPassword().isEmpty()) {
//            // Tạo mật khẩu ngẫu nhiên và gán vào request
//            String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
//            request.setPassword(generatedPassword);
//        }
    }

    @Override
    protected void beforeUpdate(Long id, UpdateCustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + id));

        if (!existingCustomer.getEmail().equals(request.getEmail()) &&
                (staffRepository.existsByEmail(request.getEmail()) || customerRepository.existsByEmail(request.getEmail()) )) {
            throw new IllegalArgumentException("Email đã được đăng kí");
        }

        if (!existingCustomer.getPhoneNumber().equals(request.getPhoneNumber()) &&
                staffRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã được đăng kí");
        }
        request.setPassword(existingCustomer.getPassword());
    }

    @Override
    protected void afterConvertCreateRequest(CreateCustomerRequest request, Customer entity) {

    }

    @Override
    protected void afterConvertUpdateRequest(UpdateCustomerRequest request, Customer entity) {

    }

    @Override
    protected String getEntityName() {
        return "Customer";
    }




    @Override
    public PageResponse<?> getAllByKeywordAndStatusAndGender(Pageable pageable, String status, String gender, String keyword) {
        int page = 0;
        if (pageable.getPageNumber() > 0) page = pageable.getPageNumber() - 1;

        Integer statusValue = null;
        if (status != null) {
            Status statusEnum = Status.fromString(status);
            if (statusEnum != null) statusValue = statusEnum.getValue();
        }
        Integer genderValue = null;
        if (gender != null) {
            Gender genderEnum = Gender.fromString(gender);
            if (genderEnum != null) genderValue = genderEnum.getValue();
        }
        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        Page<Customer> customerPage =
                customerRepository.findByKeywordContainingAndStatusAndGender(pageRequest, statusValue, genderValue, keyword);
        List<CustomerResponse> customerResponseList = mapper.toEntityDtoList(customerPage.getContent());

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .items(customerResponseList)
                .build();
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }



    @Override
    public CustomerResponse create(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail()) || staffRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email đã đã được đăng kí.");
        }
        if(customerRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new IllegalArgumentException("Số điện thoại đã được đăng kí");
        }
        String generatedPassword = request.getPassword();
        if(request.getPassword() == null) {
            // Tạo mật khẩu ngẫu nhiên và gán vào request
             generatedPassword = UUID.randomUUID().toString().substring(0, 8);
            request.setPassword(generatedPassword);
        }

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);  // Cập nhật mật khẩu đã mã hóa vào request
        // Chuyển request sang entity
        Customer entity = mapper.toCreateEntity(request);

        // Lưu entity vào cơ sở dữ liệu
        Customer savedEntity = entityRepository.save(entity);
        log.info("customer", entity.getFullName());

        if (savedEntity.getId() != null) {
            try {
                // Gửi email thông báo tài khoản
                mailService.sendLoginCustomerEmail(entity, generatedPassword);
                log.info("Registration email sent successfully to {}", entity.getEmail());
            } catch (Exception e) {
                // Log lỗi nếu gửi email thất bại
                log.error("Failed to send registration email to {}: {}", entity.getEmail(), e.getMessage());
            }
        }
        log.info("password: {}", request.getPassword());
        return mapper.toEntityDto(savedEntity);
    }



    public CustomerResponse createByOwner(RegisterCustomerRequest request) {
        if(customerRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new IllegalArgumentException("Số điện thoại đã được đăng kí");
        }
        if (customerRepository.existsByEmail(request.getEmail()) || staffRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email đã đã được đăng kí.");
        }

        if(!request.getPassword().equals(request.getPasswordComfirm())){
            throw new IllegalArgumentException("Xác nhận mật khẩu không đúng, vui lòng kiểm tra lại!");
        }

        CreateCustomerRequest createRequest = CustomerConverter.toCreateCustomerRequest(request);
        log.info("password: {}",createRequest.getPassword());
        log.info("password: {}",request.getPassword());

        // Chuyển request sang entity
        createRequest.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        Customer entity = mapper.toCreateEntity(createRequest);

        // Lưu entity vào cơ sở dữ liệu
        Customer savedEntity = entityRepository.save(entity);
        log.info("customer", entity.getFullName());

//        if (savedEntity.getId() != null) {
//            try {
//                // Gửi email thông báo tài khoản
//                mailService.sendLoginCustomerEmail(entity);
//                log.info("Registration email sent successfully to {}", entity.getEmail());
//            } catch (Exception e) {
//                // Log lỗi nếu gửi email thất bại
//                log.error("Failed to send registration email to {}: {}", entity.getEmail(), e.getMessage());
//            }
//        }
        log.info("password: {}", request.getPassword());
        return mapper.toEntityDto(savedEntity);
    }

    @Override
    public CustomerResponse changePasswordByOwner(ChangePasswordCustomerRequest request) {
        Optional<Customer> exitingCustomer = customerRepository.findByEmail(request.getEmail());
        if(exitingCustomer.isPresent()){
            Customer customer = exitingCustomer.get();

            if(checkPassword(request.getCurrentPassword(), exitingCustomer.get().getPassword())){

                String encoderNewPassword = passwordEncoder.encode(request.getNewPassword());
                customer.setPassword(encoderNewPassword);
                customerRepository.save(customer);

                return mapper.toEntityDto(customer);
            }else {
                throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
            }
        }
        else {
            throw new IllegalArgumentException("Tài khoản và mật khẩu không đúng, vui lòng kiểm tra lại");
        }
    }

    @Override
    public PageResponse<List<CustomerResponse>> ProductSalesByUser(Pageable pageable, Long id) {
        Page<CustomerResponse> customerResponsePage = customerRepositoryCustom.findProductSalesByUser(pageable,id);

        return PageResponse.<List<CustomerResponse>>builder()
                .pageNo(customerResponsePage.getNumber() + 1)
                .pageSize(customerResponsePage.getSize())
                .totalElements(customerResponsePage.getTotalElements())
                .totalPages(customerResponsePage.getTotalPages())
                .items(customerResponsePage.getContent())
                .build();
    }

    public boolean checkPassword(String currentPassword, String storedHashedPassword) {
        return passwordEncoder.matches(currentPassword, storedHashedPassword);
    }

}
