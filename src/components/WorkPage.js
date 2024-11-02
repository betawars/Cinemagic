import React, { useEffect, useRef } from "react";
import styled, { ThemeProvider } from "styled-components";
import { DarkTheme } from "./Themes";
import { motion } from "framer-motion";

import LogoComponent from "../subComponents/LogoComponent";
import SocialIcons from "../subComponents/SocialIcons";

import { Work } from "../data/WorkData";
import Card from "../subComponents/Card";
import BigTitlte from "../subComponents/BigTitlte";
import { NavLink } from "react-router-dom";

const Box = styled.div`
  background-color: ${(props) => props.theme.body};

  height: 200vh;
  position: relative;
  display: flex;
  align-items: center;
`;

const Main = styled(motion.ul)`
  position: fixed;
  top: 12rem;
  left: calc(10rem + 15vw);
  height: 40vh;
  display: flex;

  color: white;
`;


const ABOUT = styled(NavLink)`
color: ${props => props.theme.text};

position: absolute;
top: 50%;
left: calc(1rem + 2vw);
transform: translate(-50%, -50%) rotate(-90deg) ;
text-decoration: none;
z-index:1;
`

const SKILLS = styled(NavLink)`
color: ${props => props.theme.text};

position: absolute;
top: 30%;
left: calc(1rem + 2vw);
transform: translate(-50%, -50%) rotate(-90deg) ;
text-decoration: none;
z-index:1;
`

const container = {
  hidden: { opacity: 0 },
  show: {
    opacity: 1,

    transition: {
      staggerChildren: 0.5,
      duration: 0.5,
    },
  },
};

const WorkPage = () => {
  const ref = useRef(null);
  const scroll = useRef(null);

  useEffect(() => {
    let element = ref.current;

    const rotate = () => {
      element.style.transform = `translateX(${-window.scrollY}px)`;

      // return (scroll.current.style.transform =
      //   "rotate(" + -window.scrollY + "deg)");
    };

    window.addEventListener("scroll", rotate);
    return () => {
      window.removeEventListener("scroll", rotate);
    };
  }, []);

  return (
    <ThemeProvider theme={DarkTheme}>
      <SKILLS to="/skills">
        <motion.h2
          initial={{
            y: -200,
            transition: { type: 'spring', duration: 1.5, delay: 1 }
          }}
          animate={{
            y: 0,
            transition: { type: 'spring', duration: 1.5, delay: 1 }
          }}
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
        >
          My Skills
        </motion.h2>
      </SKILLS>

      <ABOUT to="/about">
        <motion.h2
          initial={{
            y: -200,
            transition: { type: 'spring', duration: 1.5, delay: 1 }
          }}
          animate={{
            y: 0,
            transition: { type: 'spring', duration: 1.5, delay: 1 }
          }}
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
        >
          About
        </motion.h2>
      </ABOUT>

      <Box>
        <LogoComponent theme="dark" />
        <SocialIcons theme="dark" />
        <Main ref={ref} variants={container} initial="hidden" animate="show">
          {Work.map((d) => (
            <Card key={d.id} data={d} />
          ))}
        </Main>

        <BigTitlte text="WORK" top="10%" right="20%" />

        <BigTitlte text="Scroll for more" top="70%" right="30%" />
      </Box>
    </ThemeProvider>
  );
};

export default WorkPage;
