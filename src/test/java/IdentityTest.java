import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class IdentityTest {
    @Test
    public void 테스트() {
        String faceListId = "test04";
        String faceListName = "test-list-name2";
        String personGroupId = "test4";
        String personGroupName = "test-group-nam2";
        String personName = "pName2";
        String winterImage = "https://www.city.kr/files/attach/images/238/084/245/027/bdccca4f42bc9b99edcdac0f6b069725.jpg";
        String karinaImage = "https://i.ytimg.com/vi/r30DLfO1D80/maxresdefault.jpg";
        String ningningImage = "https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/202202/08/04305286-7fb3-4e92-aace-c6eeb24ad1f5.jpg";
        String zizel = "https://dimg.donga.com/wps/SPORTS/IMAGE/2021/10/25/109902134.1.jpg";
        String[] images = {
                "https://dimg.donga.com/wps/SPORTS/IMAGE/2021/12/02/110575183.4.jpg",
                "https://i.ytimg.com/vi/aiOKyLqVeCc/maxresdefault.jpg",
                "http://img.sportsworldi.com/content/image/2022/01/09/20220109507818.jpg",
                "https://static.inews24.co.kr/v1/9eaa6f57d5ddc5.jpg",
                "https://img.tvreportcdn.de/cms-content/uploads/2021/11/18/5d7963d9-e669-478d-bd7a-cec95c916380.jpg",
                "http://img.luvreparis.co.kr/luvre/2021/05/ws/0518_ws1_1.jpg",
                "https://cdn.entermedia.co.kr/news/photo/202112/28297_52722_2722.jpg"
        };

        // largeFaceList 생성
        LargeFaceList faceList = LargeFaceList.builder(faceListId, faceListName).build();

        // largeFaceList에 앨범 이미지id 저장
        // 저장된 faceId와 image url 매칭
        HashMap<String, String> map = new HashMap<>();
        for (String image : images) {
            map.put(faceList.add(image), image);
        }

        // largePersonGroup 생성
        LargePersonGroup largePersonGroup =
                LargePersonGroup.builder(personGroupId, personGroupName)
                        .userData("userData")
                        .build();

        // largerPersonGroup에 person 생성
        Person person = Person.builder(largePersonGroup.getId(), personName)
                .userData("person1")
                .build();

        // person에 face 추가
        String persistedFaceId = person.addFace(largePersonGroup.getId(), winterImage);

        //

    }


}
