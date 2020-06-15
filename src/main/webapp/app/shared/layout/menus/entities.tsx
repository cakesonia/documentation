import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/client">
      Client
    </MenuItem>
    <MenuItem icon="asterisk" to="/rent">
      Rent
    </MenuItem>
    <MenuItem icon="asterisk" to="/request">
      Request
    </MenuItem>
    <MenuItem icon="asterisk" to="/fine">
      Fine
    </MenuItem>
    <MenuItem icon="asterisk" to="/car-type">
      Car Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/car">
      Car
    </MenuItem>
    <MenuItem icon="asterisk" to="/car-brand">
      Car Brand
    </MenuItem>
    <MenuItem icon="asterisk" to="/autopark">
      Autopark
    </MenuItem>
    <MenuItem icon="asterisk" to="/rental-point">
      Rental Point
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
