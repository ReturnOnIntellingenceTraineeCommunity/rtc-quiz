package net.github.rtc.quiz.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.github.rtc.quiz.Application;
import net.github.rtc.quiz.model.Answer;
import net.github.rtc.quiz.model.Difficulty;
import net.github.rtc.quiz.model.Question;
import net.github.rtc.quiz.service.QuestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class QuestionControllerTest {

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName(
      "utf8"));


    @Mock
    private QuestionService questionService;

    @InjectMocks
    QuestionController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(controller)
          .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void findAllTest() throws Exception {
        final Question question1 = createQuestion("ID1", "Question1");
        final Question question2  = createQuestion("ID2", "Question2");
        when(questionService.findAll()).thenReturn(Arrays.asList(question1, question2));
        mockMvc.perform(get("/question"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
          .andExpect(jsonPath("$", hasSize(2)))
          .andExpect(jsonPath("$[0]._id").value("ID1"))
          .andExpect(jsonPath("$[0].text").value("Question1"))
          .andExpect(jsonPath("$[1]._id").value("ID2"))
          .andExpect(jsonPath("$[1].text").value("Question2"));
        verify(questionService, times(1)).findAll();
        verifyNoMoreInteractions(questionService);
    }

    @Test
    public void paginationTest() throws Exception {
        final Question question3  = createQuestion("ID3", "Question3");
        when(questionService.findQuestionPage(2,1)).thenReturn(new PageImpl<>(Arrays.asList(question3)));
        mockMvc.perform(get("/question?limit=1&offset=2"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
          .andExpect(jsonPath("$.content[0]._id").value("ID3"))
          .andExpect(jsonPath("$.content[0].text").value("Question3"));
        verify(questionService, times(1)).findQuestionPage(2,1);
        verifyNoMoreInteractions(questionService);
    }

    @Test
    public void createQuestionTest() throws Exception {
        final Question question  = createQuestion("ID1", "Question1");
        when(questionService.save(any(Question.class))).thenReturn(question);
        mockMvc.perform(post("/question")
          .contentType(APPLICATION_JSON_UTF8)
          .content(convertObjectToJsonBytes(question)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
          .andExpect(jsonPath("$._id").value("ID1"))
          .andExpect(jsonPath("$.text").value("Question1"));
        verify(questionService, times(1)).save(any(Question.class));
        verifyNoMoreInteractions(questionService);
    }


    @Test
    public void deleteQuestionTest() throws Exception {
        mockMvc.perform(delete("/question/{id}", "ID1"))
          .andExpect(status().isOk());
    }

    @Test
    public void findQuestionByIdTest() throws Exception {
        final Question question1 = createQuestion("ID1", "Question1");
        when(questionService.findById("ID1")).thenReturn(question1);
        mockMvc.perform(get("/question/{id}","ID1"))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
          .andExpect(jsonPath("$._id").value("ID1"))
          .andExpect(jsonPath("$.text").value("Question1"));
        verify(questionService, times(1)).findById("ID1");
        verifyNoMoreInteractions(questionService);
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private Question createQuestion(String id, String text){
        Question question = new Question();
        question.set_id(id);
        question.setText(text);
        question.setDifficulty(Difficulty.EASY);
        question.setAnswers(new ArrayList<Answer>());
        return question;
    }



}
