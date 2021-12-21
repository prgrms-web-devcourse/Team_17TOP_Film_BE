# FILM

## 서비스 주소

링크 : [https://d3hatotnvqhqmx.cloudfront.net/](https://d3hatotnvqhqmx.cloudfront.net/)

## **프로젝트 설명**

**[프로젝트 Film]** 을 통해 우리는 특별한 시간을 보낸 ***장소***에 기록(필름)을 남기고, 함께 ***시간***을 보낸 사람과 추억(필름)을 공유하여 시공간을 넘은 소통을 할 수 있다.

<table align="center">
<tr>
<td align="center">최초 개봉, 열람 삭제</td>
<td align="center">마이페이지</td>
</tr>
<tr>
<td>
<img src = "https://user-images.githubusercontent.com/70435257/146948350-77e9bc78-93ed-4fb6-89ba-7d5dbfd7eaa9.gif" width="300px" height= "500px" />
</td>
<td>
<img src = "https://user-images.githubusercontent.com/70435257/146948673-f42756e6-5768-4795-85fb-267736475667.gif" width="300px" height="500px" />
</td>
</tr>
<tr>
<td align="center">필름 맡기기</td>
<td align="center">거리가 먼 필름일 경우</td>
</tr>
<tr>
<td>
<img src="https://user-images.githubusercontent.com/70435257/146948952-82abbf21-5669-4685-96af-24aa799f2516.gif" width="300px" height="500px" />
</td>
<td>
<img src="https://user-images.githubusercontent.com/70435257/146949328-085df49d-e3a9-4697-b3c2-2c4005c92744.gif" width="300px" height="500px" />
</td>
</tr>
</table>

## 팀원 소개

<table>
<tr>
<td align="center"><a href="https://github.com/2sjeong918"><img src="https://user-images.githubusercontent.com/70435257/146951570-1950d244-bbd4-439a-bb23-24a513ac72a0.png" width="150px" /></a></td>
<td align="center"><a href="https://github.com/palsa131"><img src="https://user-images.githubusercontent.com/70435257/146951411-73f25a1b-ead5-48dd-8296-e9a652200b90.png" width="150px" /></a></td>
<td align="center"><a href="https://github.com/jinn2u"><img src="https://user-images.githubusercontent.com/70435257/146951645-2939ca58-b1ef-4623-a105-12759efd1291.png" width="150px" /></a></td>
<td align="center"><a href="https://github.com/iyj6707"><img src="https://user-images.githubusercontent.com/70435257/146951725-5222c696-e5c9-45ec-b097-9d39213f3a6b.png" width="150px" /></a></td>
<td align="center"><a href="https://github.com/16min99"><img src="https://user-images.githubusercontent.com/70435257/146951967-8c757ca2-2549-4377-9e06-41cfd0543b9a.png" width="150px" /></a></td>
<td align="center"><a href="https://github.com/jungeu1509"><img src="https://user-images.githubusercontent.com/70435257/146952077-86199ea8-d1b9-4a00-a468-36ee1c5d7b93.png" width="150px" /></a></td>
</tr>
<tr>
<td align="center"><b>이소정</b></td>
<td align="center"><b>김정호</b></td>
<td align="center"><b>강동진</b></td>
<td align="center"><b>임연재</b></td>
<td align="center"><b>전민규</b></td>
<td align="center"><b>정은우</b></td>
</tr>
<tr>
<td align="center"><b>Product Owner</b></td>
<td align="center"><b>FE Developer</b></td>
<td align="center"><b>FE Developer</b></td>
<td align="center"><b>Scrum Master</b></td>
<td align="center"><b>BE Developer</b></td>
<td align="center"><b>BE Developer</b></td>
</tr>
</table>

## Github Repository

프런트 : [Team_17TOP_Film_FE](https://github.com/prgrms-web-devcourse/Team_17TOP_Film_FE)

백엔드 : [Team_17TOP_Film_BE](https://github.com/prgrms-web-devcourse/Team_17TOP_Film_BE)

---

# 백엔드 프로젝트 개요

## 개발환경
- 언어
    - Java 17
- 라이브러리
    - Spring Boot 2.5.6
    - jpa 2.6.1 hibernate
    - AWS SDK
- Database
    - H2 2.0.202
    - MySQL 8.0.25
- CI/CD
    - github actions
- 배포 환경
    - AWS EC2 / S3
- 문서화
    - Restdocs

## Entity Relationship Diagram

![ERD_Film](https://user-images.githubusercontent.com/52902010/146955622-70818c03-68ca-4827-a778-ea436136e681.png)

## CI/CD

![cicd](https://user-images.githubusercontent.com/52902010/146955932-c4a11bf7-444a-45aa-9b04-354e1b11d1a9.png)

## 구조

### 로그인 구조

![login](https://user-images.githubusercontent.com/52902010/146955940-27789345-3d0b-4946-89cf-ccbd429994e5.png)

### 멀티모듈

![multimodule](https://user-images.githubusercontent.com/52902010/146955947-314a33e4-1a3d-4aff-98f7-262d5d890f2f.png)

## Git 컨벤션

### github project 규칙

- 1 branch == 1 issue == 1 intellij task == 1 PR
- issue는 프로젝트에 연결
- pr은 issue에 연결
- 카드 관리는 작업자가 관리
- 머지도 작업자가 진행

### 브랜치 규칙

[브랜치명]

- `film/#이슈번호`

[브랜치 List]

- main
    - 배포 전에만 PR 날리는 용
- develop
    - feat이 개발된 후 바로바로 PR 날려주세요
- film/#issue_num
    - 기능 개발 용 branch

### 커밋 규칙

[커밋메세지]

- feat
- fix (리뷰 후 수정할 경우, 에러가 아닌 수정 할 경우)
- bugfix (버그일 경우)
- config
- hotfix
- style
- conflictfix (충돌 수정 시 사용)
- refactor
- comment
- doc
- test
- rename
- remove

(커밋메세지) - 간단한 한 문장

(설명) - 자세하게(선택)

예시

```md
feat: Add login feature

Connect Kakao authentication module.
```

### 이슈 규칙

 [이슈 제목]

✅ 어떤 작업을 할지 명확하게 한 문장으로 작성


[이슈 템플릿]

- 작업 내역 상세

### PR 규칙

- close #[issue 번호]로 이슈까지 닫기
- PR 후 브랜치는 각자 지우기

[PR 제목]

✅ [브랜치명] 작업 설명

[PR 템플릿]

- 작업한 내용
- 리뷰 시 참고 사항
- 테스트 결과 사진
