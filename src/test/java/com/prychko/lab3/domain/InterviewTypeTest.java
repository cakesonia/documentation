package com.prychko.lab3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.prychko.lab3.web.rest.TestUtil;

public class InterviewTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterviewType.class);
        InterviewType interviewType1 = new InterviewType();
        interviewType1.setId(1L);
        InterviewType interviewType2 = new InterviewType();
        interviewType2.setId(interviewType1.getId());
        assertThat(interviewType1).isEqualTo(interviewType2);
        interviewType2.setId(2L);
        assertThat(interviewType1).isNotEqualTo(interviewType2);
        interviewType1.setId(null);
        assertThat(interviewType1).isNotEqualTo(interviewType2);
    }
}
