package co.istad.easybanking.api.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRespository extends JpaRepository<Category,Long>{
    Category findCategoryByCategoryName(String category);
}
