/**
 * A common directive.
 * It would also be ok to put all directives into one file, or to define one RequireJS module
 * that references them all.
 */
define(['angular'], function(angular) {
    'use strict';

    var mod = angular.module('common.directives.tweets.addTweet', []);
    mod.directive('addTweet', ['$log', function($log) {
        return {
            restrict: 'AE',
            scope: true,
            templateUrl: "/assets/javascripts/common/directives/tweets/add-tweet.html",
            controller: ['$scope', '$http', function($scope, $http) {
                $scope.clearTweet= function(){
                    $scope.tweet = null;
                };
            }]
        };
    }]);
    return mod;
});
