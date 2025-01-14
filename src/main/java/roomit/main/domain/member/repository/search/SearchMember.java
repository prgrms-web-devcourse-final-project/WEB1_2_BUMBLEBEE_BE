package roomit.main.domain.member.repository.search;

import roomit.main.domain.member.dto.request.MemberUpdateRequest;

public interface SearchMember {
    void updateMember(MemberUpdateRequest memberUpdateRequest, Long memberId);
}
