// work data

import javascript from '../assets/Images/javascript.png'
import sql from '../assets/Images/sql.png'
import java from '../assets/Images/java.png'
import python from '../assets/Images/python.png'
import kotlin from '../assets/Images/kotlin.png'
import react from '../assets/Images/react.png'
import angular from '../assets/Images/angular.png'
import html from '../assets/Images/html.png'
import css from '../assets/Images/css.png'
import restapi from '../assets/Images/restapi.png'
import springboot from '../assets/Images/springboot.png'
import androidstudio from '../assets/Images/androidstudio.png'


export const cards = [
    { img: javascript, content: 'Javascript', link:'https://en.wikipedia.org/wiki/JavaScript' },
    { img: sql, content: 'SQL', link:'https://en.wikipedia.org/wiki/SQL'  },
    { img: java, content: 'JAVA', link:'https://en.wikipedia.org/wiki/Java_(programming_language)'  },
    { img: python, content: 'Python', link:'https://en.wikipedia.org/wiki/Python_(programming_language)'  },
    { img: kotlin, content: 'Kotlin', link:'https://en.wikipedia.org/wiki/Kotlin_(programming_language)'  },
    { img: react, content: 'React', link:'https://en.wikipedia.org/wiki/React_(JavaScript_library)'  },
    { img: angular, content: 'Angular', link:'https://en.wikipedia.org/wiki/Angular_(web_framework)'  },
    { img: html, content: 'HTML', link:'https://en.wikipedia.org/wiki/HTML5'  },
    { img: css, content: 'CSS', link:'https://en.wikipedia.org/wiki/CSS'  },
    { img: restapi, content: 'Rest APIs', link:'https://en.wikipedia.org/wiki/REST'  },
    { img: springboot, content: 'Spring boot', link:'https://en.wikipedia.org/wiki/Spring_Boot'  },
    { img: androidstudio, content: 'Android Studio', link:'https://en.wikipedia.org/wiki/Android_Studio'  },
    
];

export const Work = [
    {
        id:1,
        name:"Infosys",
        description:"At Infosys, I developed software solutions for the HCSC account using React, enhancing user engagement and accessibility. I optimized SQL queries, achieving an 80% reduction in response time, and gained experience with Angular 5 and JSP for the Markets account. Additionally, I reverse-engineered Java code for the Atradius account, improving efficiency by 30% with a complete front-end overhaul using React.",
        tags:["ReactJs","AngularJS","Javascript","SQL","Spring boot"],
        knowMore:"https://www.infosys.com/about.html"
    },
    {
        id:2,
        name:"Cosmo Films",
        description:"At Cosmo Films, I provided network support and troubleshooting for WAN/LAN connectivity issues, ensuring reliable communication across the organization. I installed and maintained data and voice circuits using routers, switches, and WAN devices. My efforts in configuring and troubleshooting network infrastructure contributed to a 15% improvement in system uptime.",
        tags:["Network troubleshoot","Windows"],
        knowMore:"https://www.cosmofilms.com/company-overview"
        
    },
    {
        id:3,
        name:"Employee-Portal",
        description:"WIP!! An extenstion to the existing, EMPCenter for the student worker.",
        tags:["Kotlin", "Android Studio", "Android"],
        
        demo:"",
        github:"https://github.com/betawars/Employee-Portal"
    },
    {
        id:4,
        name:"Cinemagic",
        description:"Cinemagic is a movie planner app which uses TMDB database as backend and kotlin as frontend",
        tags:["Kotlin", "Android Studio", "Android"],
        
        demo:"",
        github:"https://github.com/betawars/Cinemagic"
    },
    {
        id:5,
        name:"Offline Smartphone Assistant",
        description:"Offine Smartphone Assistant is an app developed on Android Studio using Java with the aim of giving basic functionalities like popular smartphone assistants like Siri and Google Assistant but without the need for the internet. It allows users to make a call, send a message, play music and so on, internally processing the words and not needing the use of the internet.",
        tags:["Kotlin", "Android Studio", "Android"],
        
        demo:"",
        github:""
    },
    {
        id:6,
        name:"Series Manager",
        description:"Series manager is completely developed on core java. It takes the user inpur for movies and the watch time details and store the date in MySQL table locally. ",
        tags:["Core Java, MySQL"],
        
        demo:"",
        github:""
    },
  
] 