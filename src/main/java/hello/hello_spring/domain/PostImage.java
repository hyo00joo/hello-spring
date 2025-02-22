package hello.hello_spring.domain;

import jakarta.persistence.*;

@Entity
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "postId", nullable = false)
    private Post post;


    @Lob  // 큰 바이너리 데이터를 저장할 때 사용
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String imageFile;



    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
