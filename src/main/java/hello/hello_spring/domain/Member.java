package hello.hello_spring.domain;

import jakarta.persistence.*;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

//jpa가 관리하는 entity
@Entity
public class Member {

    @Id//db가 id를 자동으로 생성해주는 것을 identity라고 함
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;  //long이 db 에서는 bignit


    //db에 있는 column name이 username으로 mapping됨
    @Column(name ="name")
    private String name;



    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();


    @OneToOne(mappedBy = "member")
//    @OneToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private ProfileImage profileImage;

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }



    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

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

    public void delete() { }
}
