package com.team.rent.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.team.rent.web.rest.TestUtil;

public class AutoparkTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autopark.class);
        Autopark autopark1 = new Autopark();
        autopark1.setId(1L);
        Autopark autopark2 = new Autopark();
        autopark2.setId(autopark1.getId());
        assertThat(autopark1).isEqualTo(autopark2);
        autopark2.setId(2L);
        assertThat(autopark1).isNotEqualTo(autopark2);
        autopark1.setId(null);
        assertThat(autopark1).isNotEqualTo(autopark2);
    }
}
