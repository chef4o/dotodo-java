import PropTypes from "prop-types";

const taskTypePropType = PropTypes.shape({
  name: PropTypes.string.isRequired,
  headerText: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
});

const sideNavPropType = PropTypes.shape({
  name: PropTypes.string.isRequired,
  icon: PropTypes.string.isRequired,
});

const connectNavPropType = PropTypes.shape({
  id: PropTypes.string.isRequired,
  url: PropTypes.string.isRequired,
  icon: PropTypes.string.isRequired,
});

const bottomNavPropType = PropTypes.shape({
  name: PropTypes.string.isRequired,
});

export {
  taskTypePropType,
  sideNavPropType,
  connectNavPropType,
  bottomNavPropType,
};
