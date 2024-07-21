package shahaf.search_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {
    private Long id;
    private Integer index;
    private String description;
}