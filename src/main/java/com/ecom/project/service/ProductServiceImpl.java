package com.ecom.project.service;

import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Cart;
import com.ecom.project.model.Category;
import com.ecom.project.model.Product;
import com.ecom.project.payload.CartDTO;
import com.ecom.project.payload.ProductDTO;
import com.ecom.project.payload.ProductResponse;
import com.ecom.project.repositories.CartRepository;
import com.ecom.project.repositories.CategoryRepository;
import com.ecom.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CartRepository  cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent = category.getProducts().stream()
                .noneMatch(p -> p.getProductName().equalsIgnoreCase(productDTO.getProductName()));

        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.jpg");
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new RuntimeException("Product with name "+ productDTO.getProductName() +" already exists");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts= productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts= productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageProducts= productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productsDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product productfromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productfromDB.setProductName(productDTO.getProductName());
        productfromDB.setDescription(productDTO.getDescription());
        productfromDB.setQuantity(productDTO.getQuantity());
        productfromDB.setDiscount(productDTO.getDiscount());
        productfromDB.setPrice(productDTO.getPrice());

        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        productfromDB.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(productfromDB);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> products = cart.getCartitems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .toList();

            cartDTO.setProducts(products);
            return cartDTO;
        }).toList();

        cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId() , productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productfromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
        String fileName = fileService.uploadImage(path, image);

        productfromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productfromDB);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }
}
