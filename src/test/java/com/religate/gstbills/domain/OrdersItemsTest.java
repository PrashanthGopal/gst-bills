package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdersItemsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdersItems.class);
        OrdersItems ordersItems1 = new OrdersItems();
        ordersItems1.setId(1L);
        OrdersItems ordersItems2 = new OrdersItems();
        ordersItems2.setId(ordersItems1.getId());
        assertThat(ordersItems1).isEqualTo(ordersItems2);
        ordersItems2.setId(2L);
        assertThat(ordersItems1).isNotEqualTo(ordersItems2);
        ordersItems1.setId(null);
        assertThat(ordersItems1).isNotEqualTo(ordersItems2);
    }
}
