package com.prychko.lab3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.prychko.lab3.web.rest.TestUtil;

public class ApplicationStatusTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationStatus.class);
        ApplicationStatus applicationStatus1 = new ApplicationStatus();
        applicationStatus1.setId(1L);
        ApplicationStatus applicationStatus2 = new ApplicationStatus();
        applicationStatus2.setId(applicationStatus1.getId());
        assertThat(applicationStatus1).isEqualTo(applicationStatus2);
        applicationStatus2.setId(2L);
        assertThat(applicationStatus1).isNotEqualTo(applicationStatus2);
        applicationStatus1.setId(null);
        assertThat(applicationStatus1).isNotEqualTo(applicationStatus2);
    }
}
