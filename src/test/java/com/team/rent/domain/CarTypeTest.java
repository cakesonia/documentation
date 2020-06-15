package com.team.rent.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.team.rent.web.rest.TestUtil;

public class CarTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarType.class);
        CarType carType1 = new CarType();
        carType1.setId(1L);
        CarType carType2 = new CarType();
        carType2.setId(carType1.getId());
        assertThat(carType1).isEqualTo(carType2);
        carType2.setId(2L);
        assertThat(carType1).isNotEqualTo(carType2);
        carType1.setId(null);
        assertThat(carType1).isNotEqualTo(carType2);
    }
}
