/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tomii;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginAsAdmin() throws Exception {
    	this.mockMvc.perform(get("/login?user=admin&password=admin")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isNotEmpty())
			.andExpect(jsonPath("$.type").value("admin"));
    }
    
    @Test
    public void loginAsRegular() throws Exception {
    	this.mockMvc.perform(get("/login?user=Derc&password=tyutyu29"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").isNotEmpty())
			.andExpect(jsonPath("$.type").value("normal"));
    }
    
    @Test
    public void invalidLogin() throws Exception {
    	this.mockMvc.perform(get("/login?user=lsdkjflsjfk&password=sdlfksdfj"))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.errorMessage").value("No user exists by this name!"));
    }
    
    @Test
    public void testRefrechCache() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/refreshcache?token=test"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testGetTopScores() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/gettopscores?token=test"))
    		.andExpect(status().isOk());
    }

    @Test
    public void testGetOwnHighScore() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/getownhighscore?token=test"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testCheckLegitimacy() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/refreshcache?token=test"));
    	this.mockMvc.perform(get("/checklegitimacy?token=test&word=word"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testSetHand() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/sethand?token=test&hand=wedswq."))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testSetInvalidHand() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	assert (this.mockMvc.perform(get("/sethand?token=test&hand=wedswq3wrwer.")).andReturn().getResponse().getContentAsString().equals("false"));
    }
    
    @Test
    public void testPlaceWord() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/placeword?word=word&x=7&y=7&down=false&token=test"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testPlaceWordOverWrite() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/placeword?word=word&x=7&y=7&down=false&token=test"))
    		.andExpect(status().isOk());
    	String shouldBeFalse = this.mockMvc.perform(get("/placeword?word=word&x=7&y=7&down=false&token=test"))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
    	assert (shouldBeFalse.equals("false"));
    }
    
    @Test
    public void testResetBoard() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/resetboard?token=test"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testSetHighScore() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/sethighscore?token=test&score=0"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testResetHighScore() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/admin/resethighscore?token=test&user=admin"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testaddWord() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/admin/addword?token=test&word=asd"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testDeleteWord() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/admin/deleteword?token=test&word=asd"))
    		.andExpect(status().isOk());
    }
    
    @Test
    public void testRegister() throws Exception {
    	this.mockMvc.perform(get("/register?user=asd&password=asd"))
    		.andExpect(status().isOk());
    	this.mockMvc.perform(get("/login?user=asd&password=asd"))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.token").isNotEmpty());
    }
    
    @Test
    public void testBanUser() throws Exception {
    	this.mockMvc.perform(get("/admin/testing?pass=f7rzv3ge"));
    	this.mockMvc.perform(get("/admin/banuser?token=test&user=asd"))
    		.andExpect(status().isOk());
    }
}
