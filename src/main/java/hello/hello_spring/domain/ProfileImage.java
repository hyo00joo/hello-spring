package hello.hello_spring.domain;

import jakarta.persistence.*;

@Entity
public class ProfileImage {

//    @Id
//    private Long id;

//    @OneToOne(mappedBy = "profileImage")

    @Id
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;


    @Lob  // 큰 바이너리 데이터를 저장할 때 사용
    @Column(name = "image_file", nullable = true, columnDefinition = "LONGTEXT")
    private String imageFile;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
