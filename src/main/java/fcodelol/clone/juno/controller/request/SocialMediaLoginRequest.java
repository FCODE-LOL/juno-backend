package fcodelol.clone.juno.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialMediaLoginRequest {
    private String socialMediaId;
    private String name;
}
