/** Common filters. */
define(['angular'], function(angular) {
  'use strict';

  var mod = angular.module('common.filters', []);

    mod.filter('hashtags', ['$sce', function($sce){
        return function(value) {
            $sce.isTrusted
            return $sce.trustAsHtml(value.replace(/(#)(\S*)/g,'<a href="#/discover">$1$2</a>'));
        }
    }]);

    mod.filter('fromNow', function(){
        return function(dateString) {
            return moment(dateString).fromNow()
        }
    });

  return mod;
});
