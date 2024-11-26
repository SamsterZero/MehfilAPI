package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.PhoneNo;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.domain.Vendor;
import in.vvm.mehfil.model.UserDTO;
import in.vvm.mehfil.repos.AddressRepository;
import in.vvm.mehfil.repos.ConsumerRepository;
import in.vvm.mehfil.repos.PhoneNoRepository;
import in.vvm.mehfil.repos.UserRepository;
import in.vvm.mehfil.repos.VendorRepository;
import in.vvm.mehfil.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConsumerRepository consumerRepository;
    private final VendorRepository vendorRepository;
    private final PhoneNoRepository phoneNoRepository;
    private final AddressRepository addressRepository;

    public UserService(final UserRepository userRepository,
            final ConsumerRepository consumerRepository, final VendorRepository vendorRepository,
            final PhoneNoRepository phoneNoRepository, final AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.consumerRepository = consumerRepository;
        this.vendorRepository = vendorRepository;
        this.phoneNoRepository = phoneNoRepository;
        this.addressRepository = addressRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setConsumer(user.getConsumer() == null ? null : user.getConsumer().getId());
        userDTO.setVendor(user.getVendor() == null ? null : user.getVendor().getId());
        userDTO.setPhoneNumbes(user.getPhoneNumbes() == null ? null : user.getPhoneNumbes().getId());
        userDTO.setAddress(user.getAddress() == null ? null : user.getAddress().getId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        final Consumer consumer = userDTO.getConsumer() == null ? null : consumerRepository.findById(userDTO.getConsumer())
                .orElseThrow(() -> new NotFoundException("consumer not found"));
        user.setConsumer(consumer);
        final Vendor vendor = userDTO.getVendor() == null ? null : vendorRepository.findById(userDTO.getVendor())
                .orElseThrow(() -> new NotFoundException("vendor not found"));
        user.setVendor(vendor);
        final PhoneNo phoneNumbes = userDTO.getPhoneNumbes() == null ? null : phoneNoRepository.findById(userDTO.getPhoneNumbes())
                .orElseThrow(() -> new NotFoundException("phoneNumbes not found"));
        user.setPhoneNumbes(phoneNumbes);
        final Address address = userDTO.getAddress() == null ? null : addressRepository.findById(userDTO.getAddress())
                .orElseThrow(() -> new NotFoundException("address not found"));
        user.setAddress(address);
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean consumerExists(final Long id) {
        return userRepository.existsByConsumerId(id);
    }

    public boolean vendorExists(final Long id) {
        return userRepository.existsByVendorId(id);
    }

}
