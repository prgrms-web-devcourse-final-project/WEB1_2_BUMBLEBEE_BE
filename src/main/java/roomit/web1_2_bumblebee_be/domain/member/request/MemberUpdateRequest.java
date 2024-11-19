package roomit.web1_2_bumblebee_be.domain.member.request;

import lombok.Builder;
import lombok.Getter;
import roomit.web1_2_bumblebee_be.domain.member.entity.Role;

@Getter
public class MemberUpdateRequest {
    private String email;
    private String phoneNumber;
    private String pwd;
    private String memberNickName;

    @Builder
    public MemberUpdateRequest( String email, String phoneNumber, String pwd, String memberNickName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pwd = pwd;
        this.memberNickName = memberNickName;
    }
}
