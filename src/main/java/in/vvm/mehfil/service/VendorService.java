package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.domain.Vendor;
import in.vvm.mehfil.model.VendorDTO;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.repos.UserRepository;
import in.vvm.mehfil.repos.VendorRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public VendorService(final VendorRepository vendorRepository,
            final ProductRepository productRepository, final UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("id"));
        return vendors.stream()
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .toList();
    }

    public VendorDTO get(final Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VendorDTO vendorDTO) {
        final Vendor vendor = new Vendor();
        mapToEntity(vendorDTO, vendor);
        return vendorRepository.save(vendor).getId();
    }

    public void update(final Long id, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorDTO, vendor);
        vendorRepository.save(vendor);
    }

    public void delete(final Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO mapToDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setId(vendor.getId());
        vendorDTO.setCompanyName(vendor.getCompanyName());
        vendorDTO.setContactName(vendor.getContactName());
        return vendorDTO;
    }

    private Vendor mapToEntity(final VendorDTO vendorDTO, final Vendor vendor) {
        vendor.setCompanyName(vendorDTO.getCompanyName());
        vendor.setContactName(vendorDTO.getContactName());
        return vendor;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Product vendorProduct = productRepository.findFirstByVendor(vendor);
        if (vendorProduct != null) {
            referencedWarning.setKey("vendor.product.vendor.referenced");
            referencedWarning.addParam(vendorProduct.getId());
            return referencedWarning;
        }
        final User vendorUser = userRepository.findFirstByVendor(vendor);
        if (vendorUser != null) {
            referencedWarning.setKey("vendor.user.vendor.referenced");
            referencedWarning.addParam(vendorUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
