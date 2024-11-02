import React, { useState } from 'react'
import styled, { keyframes, ThemeProvider } from 'styled-components'
import { NavLink } from 'react-router-dom'
import {DarkTheme} from './Themes';
import {motion} from 'framer-motion'


import LogoComponent from '../subComponents/LogoComponent';
import SocialIcons from '../subComponents/SocialIcons';
import ParticleComponent from '../subComponents/ParticleComponent';
import BigTitle from '../subComponents/BigTitlte'
import issac from '../assets/Images/issac.png'
import issacHi from '../assets/Images/issacHi.png'

const Box = styled.div`
background-color: ${props => props.theme.body};
width: 100vw;
height:100vh;
position: relative;
overflow: hidden;
`
const float = keyframes`
0% { transform: translateY(-10px) }
50% { transform: translateY(15px) translateX(15px) }
100% { transform: translateY(-10px) }

`
const fadeIn = keyframes`
0% { opacity: 0; }
100% { opacity: 1; }
`
const fadeOut = keyframes`
100% { opacity: 1; width:100% }
0% { opacity: 0; width:0% }
`

const Floating = styled.div`
position: absolute;
top: 10%;
right: 5%;
width: 20vw;
cursor:pointer;
animation: ${float} 4s ease infinite, ${fadeIn} 6s ease;
img{
    width: 100%;
    height: auto;
}
`
const FloatingDialogIn = styled.div`
position: absolute;
top: 0%;
right: 10%;
width: 20vw;
animation: ${float} 4s ease infinite, ${fadeIn} 3s ease;
img{
    width: 60%;
    height: auto;
}
`

const FloatingDialogOut = styled.div`
position: absolute;
top: 0%;
right: 10%;
width: 20vw;
animation: ${float} 4s ease infinite, ${fadeOut} 3s ease;
img{
    opacity: 0;
    width: 0%;
    height: auto;
}
`

const Main =  styled(motion.div)`
  border: 2px solid ${(props) => props.theme.text};
  color: ${(props) => props.theme.text};
  padding: 2rem;
  width: 50vw;
  height: 60vh;
  z-index: 3;
  line-height: 1.5;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: calc(0.6rem + 1vw);
  backdrop-filter: blur(4px);
  
  position: absolute;
  left: calc(5rem + 5vw);
  top: 10rem;
  font-family: 'Ubuntu Mono', monospace;
  font-style: italic;
`
const WORK = styled(NavLink)`
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



const AboutPage = () => {

    const [imageClicked, setImageClicked] = useState(false);

    const onClickHandler = () => {
        setImageClicked(!imageClicked);
      };

    return (
        <ThemeProvider theme={DarkTheme}>


            <SKILLS to="/skills">
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
                    My Skills
                </motion.h2>
            </SKILLS>


            <WORK to="/work">
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
                    Work
                </motion.h2>
            </WORK>

            <Box>

                <LogoComponent theme='dark' />
                <SocialIcons theme='dark' />
                <ParticleComponent theme='light' />

                <Floating>
                    <img src={issac} alt="Floating" onClick={()=>onClickHandler()}/>
                </Floating>
                <Main
                    initial={{ height: 0 }}
                    animate={{ height: '55vh' }}
                    transition={{ type: 'spring', duration: 2, delay: 1 }}
                >
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ duration: 1, delay: 2 }}
                    >
                        Hi! I am Shashank Sanjay Betawar.
                        <br /> <br />
                        I’m a grad student at OSU and a Software Developer. I specialize in creating user-friendly interfaces and powerful backends.
                        <br /> <br />
                        Outside of coding, I’m passionate about video games, trekking, and reading. I enjoy bringing ideas to life and would love to connect with fellow tech enthusiasts.
                        <br /> <br />
                        Let’s talk tech or chat about universe!
                        (Psst.! Don't forget me to tell your favorite videogame and/or book :))
                    </motion.div>
                </Main>

                <BigTitle text="ABOUT" top="10%" left="5%" />
                

                {imageClicked ?
                    <FloatingDialogIn>
                        <img src={issacHi} />
                    </FloatingDialogIn>
                    : <FloatingDialogOut>
                        <img src={issacHi} />
                    </FloatingDialogOut>
                }

                


            </Box>

        </ThemeProvider>
        
    )
}

export default AboutPage
