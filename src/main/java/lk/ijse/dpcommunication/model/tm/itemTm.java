package lk.ijse.dpcommunication.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class itemTm implements Comparable<itemTm>  {
    private String id;
    private String name;
    private double UnitPrice;
    private int qty;

    @Override
    public int compareTo(itemTm other) {
        return other.getId().compareTo(this.id); // Sort in descending order
    }

}
