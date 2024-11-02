import React from 'react'
import styled, { ThemeProvider } from 'styled-components'
import { DarkTheme, lightTheme } from './Themes';
import { Design, Develope } from './AllSvgs';


import LogoComponent from '../subComponents/LogoComponent';
import SocialIcons from '../subComponents/SocialIcons';
import ParticleComponent from '../subComponents/ParticleComponent';
import BigTitle from '../subComponents/BigTitlte'
import SkillCards from '../subComponents/SkillCards';
import { motion } from 'framer-motion';
import { NavLink } from 'react-router-dom';

const Box = styled.div`
background-color: ${props => props.theme.body};
width: 100vw;
height:100vh;
position: relative;
display: flex;
justify-content: space-evenly;
align-items: center;


`

const Main = styled(motion.div)`
border: 2px solid ${props => props.theme.text};
color: ${props => props.theme.text};
padding: 2rem;
width: 75vw;
height: 70vh;
z-index:3;
line-height: 1.5;
backdrop-filter: blur(4px);
margin-right:5vw;

font-family: 'Ubuntu Mono',monospace;
display: flex;
justify-content: space-between;
align-items: center;

`

const Title = styled.h2`
padding-left: 2vw;
display: flex;
justify-content: center;
align-items: center;
font-size: calc(1em + 1vw);

&>*:first-child{
margin-right: 1rem;
}
`

const ABOUT = styled(NavLink)`
color: ${props => props.theme.text};

position: absolute;
top: 50%;
left: calc(1rem + 2vw);
transform: translate(-50%, -50%) rotate(-90deg) ;
text-decoration: none;
z-index:1;
`

const Description = styled.div`
color: ${props => props.theme.text};
font-size: calc(0.6em + 1vw);
padding: 0.5rem 0;


strong{
    margin-bottom: 1rem;
    text-transform: uppercase;
}
ul,p{
    margin-left: 2rem;
}
`

const WORK = styled(NavLink)`
color: ${props => props.click ? props.theme.body : props.theme.text};

position: absolute;
top: 30%;
left: calc(1rem + 2vw);
transform: translate(-50%, -50%) rotate(-90deg) ;
text-decoration: none;
z-index:1;
`

const MySkillsPage = () => {

    const innerDivStyle = {
        display: "flex",
        justifyContent: 'space-between',
        alignItems: 'center'
    }

    return (
        <ThemeProvider theme={DarkTheme}>

<WORK to="/work">
                <motion.h2
                initial={{
                    y:-200,
                    transition: { type:'spring', duration: 1.5, delay:1}
                }}
                animate={{
                    y:0,
                    transition: { type:'spring', duration: 1.5, delay:1}
                }}
                 whileHover={{scale: 1.1}}
                whileTap={{scale: 0.9}}
                >
                    Work
                </motion.h2>
            </WORK>

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

                <LogoComponent theme='dark' />
                <SocialIcons theme='dark' />
                <ParticleComponent theme='light' />
                <Main
                    initial={{ height: 0 }}
                    animate={{ height: '55vh' }}
                    transition={{ type: 'spring', duration: 2, delay: 1 }}
                >

                    <motion.div
                        style={innerDivStyle}
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 1, delay: 2 }}
                    >
                        <Title>
                            Click to learn more about them
                        </Title>
                        <SkillCards />
                        


                    </motion.div>
                    <BigTitle text="SKILLS" top="80%" right="10%" />

                </Main>

                

            </Box>

        </ThemeProvider>

    )
}

export default MySkillsPage
