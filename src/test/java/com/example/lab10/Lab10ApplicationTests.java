package com.example.lab10;

import com.example.lab10.model.Product;
import com.example.lab10.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

/**
 * Lab10ApplicationTests — ทดสอบ Reactive code
 *
 * ✅ test findById() ทำเสร็จแล้วเป็นตัวอย่าง
 * ✅ เพิ่ม test สำหรับ reactive repository แล้ว
 *
 * StepVerifier — วิธีทดสอบ Mono/Flux:
 *   StepVerifier.create(mono/flux)
 *     .expectNext(value)     ← คาดหวังค่าที่ได้
 *     .expectNextCount(n)    ← คาดหวังจำนวน element
 *     .verifyComplete()      ← ยืนยัน onComplete
 *     .verifyError()         ← ยืนยัน onError
 */
@SpringBootTest
class Lab10ApplicationTests {

    @Autowired
    private ProductRepository repository;

    // ══════════════════════════════════════════════════════
    // ✅ ตัวอย่าง test — ศึกษาแล้วเพิ่ม test เอง
    // ══════════════════════════════════════════════════════

    @Test
    void contextLoads() {
        // Spring Application Context โหลดสำเร็จ
    }

    @Test
    void testFindById_found() {
        // ✅ ตัวอย่าง: ทดสอบ findById ที่พบข้อมูล
        StepVerifier.create(repository.findById("1"))
                .expectNextMatches(p -> p.getName().contains("iPhone"))
                .verifyComplete();
    }

    @Test
    void testFindById_notFound() {
        // ✅ ตัวอย่าง: ทดสอบ findById ที่ไม่พบข้อมูล
        StepVerifier.create(repository.findById("999"))
                .verifyComplete(); // Mono.empty() → onComplete ทันที
    }

    // ══════════════════════════════════════════════════════
    // Tests ที่เพิ่มสำหรับ method ที่ implement
    // ══════════════════════════════════════════════════════

    @Test
    void testFindAll() {
        StepVerifier.create(repository.findAll()
                        .filter(product -> product.getId().matches("[1-3]")))
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testSave() {
        Product product = new Product("test-4", "Reactive Programming Book",
                "Books", "CP353002", 10, 590.0, "NONE");

        StepVerifier.create(repository.save(product))
                .expectNext(product)
                .verifyComplete();

        StepVerifier.create(repository.findById("test-4"))
                .expectNext(product)
                .verifyComplete();

        repository.deleteById("test-4").block();
    }

    @Test
    void testFindByCategory() {
        StepVerifier.create(repository.findByCategory("electronics"))
                .expectNextCount(3)
                .verifyComplete();
    }
}
