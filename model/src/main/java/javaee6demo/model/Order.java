package javaee6demo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Matija Mazi <br/>
 * @created 9.1.12 17:22
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {
    public static final Random RANDOM = new Random();

    @Id @Size(max = 40)
    @XmlAttribute(required = true)
    @XmlID
    private String id;

    private Date created;

    @Version
    private Date updated;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("product")
    private List<OrderItem> items = new ArrayList<OrderItem>();

    @ManyToOne
    @XmlIDREF
    private User creator;

    @ManyToOne
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(length = 31)
    private Status status;

    private String comments;

    private Double total;

    protected Order() { }

    public Order(User creator) {
        this.creator = creator;
        this.assignee = creator;
        created = new Date();
        status = Status.BSK;
    }

    public String getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean addItem(OrderItem item) {
        item.setOrder(this);
        return items.add(item);
    }

    public void addItem(Product product, Double quantity) {
        addItem(new OrderItem(this, product, quantity));
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public User getCreator() {
        return creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Order[{0} {1}, created {2}, assigned to {3}]", id, status, created, assignee);
    }

    @PrePersist
    public void prePersist() {
        this.id = MessageFormat.format("O/{0,date,short}_{1}", new Date(), Math.abs(RANDOM.nextInt(10000)));
    }

    public enum Status {
        BSK("Basket"),
        CNF("Confirmed"),
        PAID("Paid"),
        PKG("Packaging"),
        SNT("Sent"),
        DLV("Delivered"),
        ;

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
