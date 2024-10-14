package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.ProductDto;
import com.inventory_management.web.mapper.ProductMapper;
import com.inventory_management.web.entity.Product;
import com.inventory_management.web.repository.ProductRepository;
import com.inventory_management.web.repository.TypeRepository;
import com.inventory_management.web.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    TypeRepository typeRepository;

    public ProductServiceImpl(ProductRepository productRepository, TypeRepository typeRepository) {
        this.productRepository = productRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    public int getTotalQuantityByProductType(Long productType_id) {
        Integer totalQuantity = productRepository.getTotalQuantityByProductType(productType_id);
        return (totalQuantity != null) ? totalQuantity : 0;
    }

    @Override
    public boolean existsByTypeId(long typeID) {
        return productRepository.existsById(typeID);
    }

    @Override
    public List<ProductDto> findAllProductsDto() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper :: toDto).collect(Collectors.toList());
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        // Convert DTO to entity
        Product product = ProductMapper.toEntity(productDto);
        product.setSo_luong_hien_tai(productDto.getNumberGet());
        product.setSo_luong_da_nhap(productDto.getNumberGet());
        product.setTong_von_san_pham(productDto.getPriceGet());

        // Get the uploaded file from the DTO
        MultipartFile imageFile = productDto.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Define the path to save the image
                String uploadDirectory = "D:/Project/Inventory Management/web/src/main/resources/static/img/productImg";
                String imageName = imageFile.getOriginalFilename(); // Get original image name
                String filePath = uploadDirectory + "/" + imageName;

                // Save the file to the specified directory
                File destinationFile = new File(filePath);
                imageFile.transferTo(destinationFile);

                // Save the relative path to the database
                product.setImagePath("/img/productImg/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (log, show message, etc.)
            }
        }

        // Save the product entity to the database
        productRepository.save(product);
        // Update số lượng sản phẩm của thể loại
        typeRepository.updateNumOfProduct();
    }




    @Override
    public void delete(long productID) {
        productRepository.deleteById(productID);
    }


    @Override
    public void updateProduct(ProductDto productDto) {

        Product product = ProductMapper.toEntity(productDto);
        MultipartFile imageFile = productDto.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Define the path to save the image
                String uploadDirectory = "D:/Project/Inventory Management/web/src/main/resources/static/img/productImg";
                String imageName = imageFile.getOriginalFilename(); // Get original image name
                String filePath = uploadDirectory + "/" + imageName;

                // Save the file to the specified directory
                File destinationFile = new File(filePath);
                imageFile.transferTo(destinationFile);

                // Save the relative path to the database
                product.setImagePath("/img/productImg/" + imageName);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (log, show message, etc.)
            }
        }

        productRepository.save(product);
        // Update số lượng sản phẩm của thể loại
        typeRepository.updateNumOfProduct();
    }

    @Override
    public ProductDto findById(Long producID) {
        Product product = productRepository.findById(producID).orElse(null);
        return ProductMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> searchProducts(String name, Long startPrice, Long endPrice, Pageable pageable, Integer productTypeId) {
        Page<Product> productsPage = productRepository.searchProduct(name, startPrice, endPrice, productTypeId, pageable);
        return productsPage.map(ProductMapper::toDto);
    }

    @Override
    public Product findByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Override
    public void save(ProductDto product) {
        productRepository.save(ProductMapper.toEntity(product));
    }
}
