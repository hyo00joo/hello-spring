package hello.hello_spring.domain;

import jakarta.persistence.*;

//jpa가 관리하는 entity
@Entity
public class Member {

    @Id
    //db가 id를 자동으로 생성해주는 것을 identity라고 함
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;  //long이 db 에서는 bignit


    //db에 있는 column name이 username으로 mapping됨
    @Column(name = "username")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
