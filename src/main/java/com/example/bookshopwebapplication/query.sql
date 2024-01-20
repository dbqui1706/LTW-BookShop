select * from product;
select * from category;
delete from category where id = 28;
select * from product_category where categoryId=2;
select * from user;
delete from user where id = 32;
select * from product_review;
UPDATE product_review
SET isShow = 1, updatedAt = NOW() WHERE id = 151;

SELECT *
FROM product p
WHERE p.id IN (SELECT productId
             FROM product_category pc
             JOIN category c ON pc.categoryId = c.id
             WHERE c.name IN ('Sách khoa học'));

SELECT *
FROM product p
WHERE p.id IN (SELECT pc.productId
               FROM product_category pc
                   JOIN category c WHERE c.name in ('Sách giáo khoa'))
-- AND p.publisher IN ('', '') AND (p.price BETWEEN 10 AND 20)
--
-- SELECT DISTINCT (publisher)
-- FROM product

SELECT * FROM order_item;