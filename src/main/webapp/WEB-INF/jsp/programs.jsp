<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Programs</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    body {font-family: "Open sans";}
    h1, h2, h3, h4, h5, h6 {
        font-family: "PT serif";
    }
    .w3-tag, .fa {cursor:pointer}
    .w3-tag {height:15px;width:15px;padding:0;margin-top:6px}
</style>
<body style="background-color: #F5F5F5; color: black">>
<jsp:include page="component/header.jsp" flush="true"/>

<div class="w3-content" style="max-width:1100px;margin-top:40px;margin-bottom:80px">
    <p class="w3-center" style="font-size: 48px;  margin-bottom: 0; margin-top: 10px; font-weight: bold; font-family: 'PT serif';">OUR PLANS</p>
    <div class="w3-display-container w3-center">
        <img src="../../images/gym4.jpg" style="width:100%">
    </div>

    <div class="w3-row w3-container" style="font-size: 20px">
        <div class="w3-center w3-padding-64">
            <span class="w3-bottombar w3-border-dark-grey w3-padding-16" style="font-size: 32px">Specializations</span>
        </div>
        <div class="w3-col l3 m6 w3-light-grey w3-container w3-padding-16">
            <p>Body Fat Reduction & Habit Alteration.</p>
        </div>

        <div class="w3-col l3 m6 w3-grey w3-container w3-padding-16">
            <p>Nutrition Coaching & Meal Planning.</p>

        </div>

        <div class="w3-col l3 m6 w3-dark-grey w3-container w3-padding-16">
            <p>Strength & Conditioning Coaching. </p>
        </div>

        <div class="w3-col l3 m6 w3-black w3-container w3-padding-16">
            <p>Physique & Figure Training.</p>
        </div>
    </div>

    <div class="w3-row w3-container" style="font-size: 20px">
        <div class="w3-center w3-padding-64">
            <span class="w3-bottombar w3-border-dark-grey w3-padding-16" style="font-size: 32px">Our results</span>
        </div>
        <div class="w3-display-container w3-center" >
            <img src="../../images/beforeafter2.png" style="width:100%">
        </div>
        <div class="w3-display-container w3-center" style="padding-top: 20px">
            <img src="../../images/beforeafter.png" style="width:100%">
        </div>
        <div class="w3-display-container w3-center" style="padding-top: 20px">
            <img src="../../images/beforeafter3.png" style="width:100%">
        </div>
    </div>

    <div class="w3-row-padding" id="plans">
        <div class="w3-center w3-padding-64">
            <h3>Pricing Plans</h3>
            <p>Choose a pricing plan that fits your needs.</p>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-black w3-xlarge w3-padding-32">Basic</li>
                <li class="w3-padding-16"> Custom strength training workouts <b>based on what equipment</b> you have available</li>
                <li class="w3-padding-16">Personalized cardio programming from <b>beginner to advanced</b>techniques</li>
                <li class="w3-padding-16">
                    <b>Flexibility & mobility</b> programs for your unique needs</li>
                <li class="w3-padding-16">
                    One <b>30 minute video</b> chat coaching session monthly</li>
                <li class="w3-padding-16">
                    <h2 class="w3-wide">$10</h2>
                    <span class="w3-opacity">per month</span>
                </li>

                <li class="w3-light-grey w3-padding-24">
                    <a href="${pageContext.request.contextPath}/controller?command=show_signup" class="w3-button w3-white w3-padding-large">Sign Up</a>
                </li>
            </ul>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-dark-grey w3-xlarge w3-padding-32">Pro</li>
                <li class="w3-padding-16">
                    <b>One custom meal plan</b> written by a Registered Dietitian</li>
                <li class="w3-padding-16"> Custom strength training workouts <b>based on what equipment</b> you have available</li>
                <li class="w3-padding-16">Personalized cardio programming from <b>beginner to advanced</b>techniques</li>
                <li class="w3-padding-16">
                    <b>Flexibility & mobility</b> programs for your unique needs</li>
                <li class="w3-padding-16">
                    <b>Two 30 minute video</b> chat coaching session monthly</li>

                <li class="w3-padding-16">
                    <h2 class="w3-wide">$ 30</h2>
                    <span class="w3-opacity">per month</span>
                </li>
                <li class="w3-light-grey w3-padding-24">
                    <a href="${pageContext.request.contextPath}/controller?command=show_signup" class="w3-button w3-white w3-padding-large">Sign Up</a>
                </li>
            </ul>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-black w3-xlarge w3-padding-32">Premium</li>
                <li class="w3-padding-16">
                    <b>Unlimited</b> in-app messaging with your personal coach</li>
                <li class="w3-padding-16">
                    <b>One custom meal plan</b> written by a Registered Dietitian</li>
                <li class="w3-padding-16"> Custom strength training workouts <b>based on what equipment</b> you have available</li>
                <li class="w3-padding-16">Personalized cardio programming from <b>beginner to advanced</b>techniques</li>
                <li class="w3-padding-16">
                    <b>Flexibility & mobility</b> programs for your unique needs</li>
                <li class="w3-padding-16">
                    One <b>30 minute video</b> chat coaching session <b>weekly</b></li>

                <li class="w3-padding-16">
                    <h2 class="w3-wide">$ 50</h2>
                    <span class="w3-opacity">per month</span>
                </li>
                <li class="w3-light-grey w3-padding-24">
                    <a href="${pageContext.request.contextPath}/controller?command=show_signup" class="w3-button w3-white w3-padding-large">Sign Up</a>
                </li>
            </ul>
        </div>
    </div>
</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>
