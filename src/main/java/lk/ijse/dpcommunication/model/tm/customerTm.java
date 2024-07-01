package lk.ijse.dpcommunication.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode

public class customerTm implements Comparable<customerTm>  {
    private String id;
    private String name;
    private String tel;

    @Override
    public int compareTo(customerTm other) {
        return other.getId().compareTo(this.id); // Sort in descending order
    }
}
