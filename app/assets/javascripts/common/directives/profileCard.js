/**
 * A common directive.
 * It would also be ok to put all directives into one file, or to define one RequireJS module
 * that references them all.
 */
define(['angular'], function(angular) {
  'use strict';

  var mod = angular.module('common.directives.profileCard', []);
  mod.directive('profileCard', ['$log', function($log) {
    return {
      restrict: 'AE',
      scope: {
        forUsername: '@'
      },
      templateUrl: "/assets/javascripts/common/directives/profile-card.html"
    };
  }]);
  return mod;
});