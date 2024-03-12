package com.ye13sh.ProductService.Service;

import com.ye13sh.ProductService.DTO.Cart;
import com.ye13sh.ProductService.DTO.CommonDTO;
import com.ye13sh.ProductService.Model.Product;
import com.ye13sh.ProductService.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private static final String orderURL="http://localhost:7070/buyNow/order/";

    private static final String deleteCartURL= "http://localhost:9090/delete/cart/";

    private static final String updateCartProductURL = "http://localhost:9090/update/cartProduct/";

    public static String generateRandomUUID() {
        Random random = new Random();
        int randomNumber = 1000+random.nextInt(10000); // Generate a random number between 1000 and 9999
        return String.format("%04d", randomNumber); // Format the number to have exactly 4 digits
    }
    public Product insertProduct(CommonDTO dto){
        Product product=new Product();

        product.setProductName(dto.getProductName());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        //product.setTotalPrice();
        product.setProductID(generateRandomUUID());

        return productRepository.save(product);
    }
    public Page<Product> getAllProduct(int numbers){
        Pageable pageable = PageRequest.of(numbers,10);
        return productRepository.findAll(pageable);
    }
    public Product getProductByName(String productName){
        return productRepository.getByProductName(productName);
    }
    public void updateProduct(CommonDTO dto,String productName){// if price in product is update even in cart will also update
        Product product= productRepository.findByProductName(productName);
        Cart cart = new Cart();

        if(product!=null){
            product.setPrice(dto.getPrice());
            product.setQuantity(dto.getQuantity());
            cart.setPrice(dto.getPrice()); // product-service(cart) dto & product-service(CommonDTO) dto will connect with cart-service dto
            productRepository.save(product);
        }
        restTemplate.put(updateCartProductURL+productName,cart);
    }


//    public Product updateCartProduct(CommonDTO dto,String productName){ // not in use by cart-service
//        Product product = productRepository.findByProductName(productName);
//
//        product.setQuantity(dto.getQuantity());
//        return productRepository.save(product);
//    }


    public void DeleteProductByName(String productName){
        productRepository.deleteByProductName(productName);

//        TransactionTemplate transactionTemplate=new TransactionTemplate(transactionManager);
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
//                if(product.getProductName()!=null){
//                    productRepository.deleteByProductName(productName);
//                }
//            }
//        });
    }
    public void insertCart(CommonDTO dto,String productName){
        Cart cart=new Cart();
        Product product=productRepository.findByProductName(productName);

        //String encodedProductName = URLEncoder.encode(product.getProductName());
        String InsertCart="http://localhost:9090/insert/cart/"+product.getProductName();

        if(product!=null && product.getQuantity() > dto.getQuantity()){
            cart.setProductName(product.getProductName());
            cart.setQuantity(dto.getQuantity());
            cart.setPrice(product.getPrice());
            cart.setProductID(product.getProductID());
        }else {
            throw new RuntimeException("product not found or Quantity is exceeded");
        }
        //Firstly set all in cart and then call the URL (***Don't call URL first***)
        ResponseEntity<String> response = restTemplate.postForEntity(InsertCart, cart, String.class); //check with ResponseEntity<Cart>
        String responseBody = response.getBody();

        //if cart service used ResponseEntity<Cart> in its controller then you should use Cart.class instead of String.class
    }

    public Product subProductQuantity(Cart cart,String productName){
        Product product=productRepository.findByProductName(productName);

        product.setQuantity(product.getQuantity() - cart.getQuantity());

        return productRepository.save(product);
    }

    public void buyNowOrder(Cart cart,String productName) {
        CommonDTO dto = new CommonDTO();

        Product product = productRepository.findByProductName(productName);

        if (product != null && product.getQuantity() > cart.getQuantity()) {
            dto.setProductName(product.getProductName());
            dto.setQuantity(cart.getQuantity());
            dto.setTotalPrice(cart.getQuantity() * product.getPrice());
            dto.setProductID(product.getProductID());
        }else {
            throw new RuntimeException("product not found or Quantity is exceeded");
        }

        //ResponseEntity<String> used in order-service to place order
        ResponseEntity<String> response= restTemplate.postForEntity(orderURL+product.getProductName(),dto,String.class);
        String responseBody=response.getBody();
    }

    public void deleteCartProduct(String productName){
        Product product = productRepository.findByProductName(productName);

       restTemplate.delete(deleteCartURL+productName);
    }

}
