package com.example.usedTrade;
/*
import com.example.usedTrade.DTO.Request.Users.UserRequestDto;
import com.example.usedTrade.Entity.Users.UserRole;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.ItemPost.ItemPostRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.ItemPost.ItemPostCommadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
class UsedTradeApplicationTests {

	@Autowired
	private ItemPostRepository itemPostRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ItemPostCommadService itemPostCommadService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}


	List<MultipartFile> files = null;

 */
/*
	@Test
	void 더미데이터() throws IOException {
		for(int i =50; i<800; i++)
		{
			int k = i%10;

			Random random = new Random();
			Long number = (long) (random.nextInt(20) + 1);


			ItemPostRequestDto itemPostRequestDto = new ItemPostRequestDto("", 1,"",number);
			itemPostRequestDto.setTitle("테스트 제목 : "+ i);
			itemPostRequestDto.setPrice(1000*(i+1));
			itemPostRequestDto.setContent("테스트 내용 : "+ i);

			itemPostCommadService.createItemPost(itemPostRequestDto,"testUser" +k,files);
		}
	}


 */

/*
	@Test
	void 더미유저()
	{
		for(int i =0; i<10; i++)
		{
			UserRequestDto dto = new UserRequestDto("testUser" + i,"테스트닉네임" +i,
					"test" +i+"@naver.com","123123", UserRole.NORMAL);


			Users users = dto.toEntity(passwordEncoder.encode("123123"));
			userRepository.save(users);
		}
	}

 */
// }
