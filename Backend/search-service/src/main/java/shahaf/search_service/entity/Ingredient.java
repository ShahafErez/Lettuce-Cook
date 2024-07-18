package shahaf.search_service.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shahaf.search_service.enums.Unit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private Long id;
    private String name;
    private Unit unit;
    private Float amount;

}
