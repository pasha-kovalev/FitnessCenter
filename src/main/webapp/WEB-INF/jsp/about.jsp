<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>About</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    body {font-family: "Open sans";}
    h1, h2, h3, h4, h5, h6 {
        font-family: "PT serif";
    }
</style>
<body>
<jsp:include page="component/header.jsp" flush="true"/>
<div class="w3-content" style="max-width:1100px">
    <h1 class="w3-center" style="margin-top: 72px">MEET OUR TEAM</h1>
    <div class="w3-row w3-padding-64">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/gigachad.png" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">Gregory Gigachadskiy</h1>
            <br><br><br>
            <p class="w3-large">
                The founder. Gregory is a former college instructor and consultant to the Advanced Personal Training
                and Exercise Science program offered by Bryan University. Further, he is an advisor, author and
                consultant to multiple fitness companies and publications in the United States. Michael is ranked as one
                of the best online personal trainers and habit coaches.
            </p>
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64">
        <div class="w3-col l6 w3-padding-large">
            <h1 class="w3-left">Roman Goslinov</h1><br>
            <br><br><br>
            <p class="w3-large">I am driven to guide others through their fitness and wellness journey. Every person
                must take ownership of their health, and it is my mission to empower others with the skills necessary
                to do so. I earned a Bachelor’s Degree from the University of Vermont in Exercise and Movement Science
                and hold several fitness certifications. Through continuing education, I have retained the Certified
                Strength and Conditioning Specialist credential from the National Strength and Conditioning Association
                across three cycles since 2011. My experience includes time working with Division I college athletes, in
                an outpatient physical therapy setting, as a group fitness instructor, small group trainer, and many
                years as a personal trainer and habit coach. I am a positive-minded and supportive coach who enjoys
                the process of physical and psychological change.
            </p>
        </div>

        <div class="w3-col l6 w3-padding-large">
            <img src="../../images/gosling.jpg" class="w3-round w3-image" width="600" height="750">
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/floppa.jpg" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">Philipp Bolshov</h1>
            <br><br><br>
            <p class="w3-large">
                I possess nearly a decade of experience as a Certified Personal trainer and am driven by my desire to
                learn how to help people reach their physical and personal goals. I received my Associates Degree from
                Bryan University in Advanced Personal Training and Exercise Science and now work for Bryan University
                as an adjunct instructor within that program. I continue to learn as much as I can in the field as well
                as educate others. As a lifelong learner, the opportunity to share my knowledge and learn from others
                drives my dedication to my clients and my profession.
            </p>
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64" id="menu">
        <div class="w3-col l6 w3-padding-large">
            <h1 class="w3-left">Georgiy Izriveev</h1><br>
            <br><br>
            <p class="w3-large">Participant in the Afghan war. Loves unicorns.</p>
        </div>

        <div class="w3-col l6 w3-padding-large">
            <img src="../../images/geralt.png" class="w3-round w3-image" width="600" height="750">
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64" id="about">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/karamba.jpg" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">Karina Egamedieva</h1>
            <br><br><br>
            <p class="w3-large">
                Karina is a Registered Dietitian, Certified Personal Trainer and Group Fitness Instructor.
                She graduated from Oregon State University with a degree in Nutrition & Dietetics, minor in Women's
                Studies, and completed her supervised practice program at Montana State University focusing on
                sustainable food systems and rural healthcare. Anna has 5 years’ experience in the wellness industry
                as a fitness professional practicing joyful movement, was the nutrition program coordinator at
                Lifetime Sky, and a leading instructor for the National Diabetes Prevention Program. She now works
                as a clinical dietitian at the Iris House Women's HIV Center and is the official Registered Dietitian
                of Forge Fitness and Nutrition.
            </p>
        </div>
    </div>

</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>

