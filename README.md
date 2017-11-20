#Like Animation View
-----------------------------------------------------------------------

## Introduction

A custom view that show you like something. This view has animation which shows like this：

![Gif example](https://github.com/fredliao123/LikeAnimationView/blob/master/app-release/gif/2.gif)

[app-release.apk](https://github.com/fredliao123/LikeAnimationView/blob/master/app-release/app-release.apk)

## Animation

-Thumb part:

1.Gray thumb first shrinks a little. 2. Thumb turn into read thumb and size restores. 3. A red circle spread out from central meanwhile the dot above shows.

-Number part:

1.Only the numbers that changes move from bottom to top.

---------------------------------------------------------------------------------------------------------------

##To Use This View
Just type in you gradle

`compile 'bupt.liao.fred:LikeAnimationView:1.0.0`

And to use in xml:
```
<bupt.liao.fred.lavview.view.LikeUpView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:lav_count="110"></bupt.liao.fred.lavview.view.LikeUpView>
```
