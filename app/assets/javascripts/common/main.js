/**
 * Common functionality.
 */
define(['angular', './services/helper', './services/playRoutes', './filters', './directives/example', './directives/profileCard'],
    function(angular) {
  'use strict';

  return angular.module('yourprefix.common', ['common.helper', 'common.playRoutes', 'common.filters',
    'common.directives.example','common.directives.profileCard']);
});
