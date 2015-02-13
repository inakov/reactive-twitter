/**
 * A common directive.
 * It would also be ok to put all directives into one file, or to define one RequireJS module
 * that references them all.
 */
define(['angular'], function(angular) {
    'use strict';

    var mod = angular.module('common.directives.follow.followButton', []);
    mod.directive('followButton', ['$log', function($log) {
        return {
            restrict: 'AE',
            scope: {
                user: '@',
                following: '@'
            },
            templateUrl: "/assets/javascripts/common/directives/follow/follow-button.html",
            controller: ['$scope', '$http', function($scope, $http) {
                $scope.followButtonClick = function(){
                    if($scope.following){
                        $scope.unfollow();
                    }else{
                        $scope.follow();
                    }
                    $scope.following = !$scope.following;
                }

                $scope.buttonText = function(){
                    if($scope.following){
                        return "Following";
                    }else{
                        return "Follow";
                    }
                }

                $scope.follow = function(){
                    $http({
                        method: 'POST',
                        url: "http://localhost:9000/follow",
                        data: { "follow": $scope.user}
                    }).success(function(data) {
                        console.log("Followed - user: " + $scope.user);
                    });
                };

                $scope.unfollow = function(){
                    $http({
                        method: 'POST',
                        url: "http://localhost:9000/unfollow",
                        data: { "unfollow": $scope.user}
                    }).success(function(data) {
                        console.log("Unfollowed - user: " + $scope.user);
                    });
                };
            }],
            link: function (scope, element) {
                element.on('mouseenter', function() {
                    var subelement = element.find('.follow-button');
                    if(scope.following){
                        subelement.addClass("btn-danger");
                        subelement.removeClass("btn-primary");
                        subelement.text("Unfollow");
                    }else{
                        subelement.addClass('btn-primary');
                    }
                });
                element.on('mouseleave', function() {
                        var subelement = element.find('.follow-button');
                        if(scope.following){
                            subelement.addClass("btn-primary");
                            subelement.text("Following");
                        }else{
                            subelement.removeClass("btn-primary");
                            subelement.text("Follow");
                        }
                        subelement.removeClass('btn-danger');
                });
                element.on('click', function() {
                    var subelement = element.find('.follow-button');
                    if(scope.following){
                        subelement.addClass("btn-primary");
                        subelement.text("Following");
                    }else{
                        subelement.removeClass("btn-primary");
                        subelement.text("Follow");
                    }
                    subelement.removeClass('btn-danger');
                });
            }
        };
    }]);
    return mod;
});
