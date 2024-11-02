import { motion } from 'framer-motion';
import React from 'react'
import { NavLink } from 'react-router-dom';
import styled, { createGlobalStyle } from 'styled-components';
import { Github } from '../components/AllSvgs';

const GlobalStyle = createGlobalStyle`
  ::-webkit-scrollbar {
    display: none;
  }

  body {
    overflow: -moz-hidden;
  }

  body {
    -ms-overflow-style: none; 
  }

`;



const Box = styled(motion.li)`
width: 16rem;
height: 55vh;
background-color: ${props => props.theme.text};
color:${props => props.theme.body};
padding: 1.5rem 2rem;
margin-right: 4rem;
border-radius: 0 50px 0 50px;
display: flex;
flex-direction: column;
justify-content: space-between;
border: 1px solid ${props => props.theme.body};
transition: all 0.2s ease;

&:hover{
background-color: ${props => props.theme.body};
color:${props => props.theme.text};
border: 1px solid ${props => props.theme.text};

}
`
const Title = styled.h2`
font-size: calc(1em + 0.5vw);
margin-bottom: 0.2em;
`

const Description = styled.h2`
font-size: calc(0.8em + 0.3vw);
font-family: 'Karla',sans-serif;
font-weight: 500;
`
const Tags = styled.div`
border-top: 2px solid ${props => props.theme.body};
padding-top: 0.5rem;
display:flex;
flex-wrap:wrap;
${Box}:hover &{
border-top: 2px solid ${props => props.theme.text};
}
`
const Tag = styled.span`
margin-right:1rem;
font-size:calc(0.8em + 0.3vw);
`

const Footer = styled.footer`
display: flex;
justify-content: space-between;
`

const Link = styled.a`
background-color: ${props => props.theme.body};
color: ${props => props.theme.text};
text-decoration: none;
padding:0.5rem calc(2rem + 2vw);
border-radius: 0 0 0 50px;
// font-size:calc(1em + 0.5vw);
font-size:2vh;

${Box}:hover &{
    background-color: ${props => props.theme.text};
    color: ${props => props.theme.body};

}
`

const Git = styled.a`
color: inherit;
text-decoration: none;
${Box}:hover &{
    &>*{
        fill:${props => props.theme.text};
    }
}

`

const Item = {
    hidden: {
        scale: 0
    },
    show: {
        scale: 1,
        transition: {
            type: 'spring',
            duration: 0.5
        }
    }
}

const Card = (props) => {

    const { id, name, description, tags, demo, github, knowMore } = props.data;


    return (
        
        <Box key={id} variants={Item}>
            <GlobalStyle/>
            <div>
                <Title>{name}</Title>
                <Description>
                    {description}
                </Description>
            </div>
            

            <div>
                <p>Skills:</p>
                <Tags style={demo && tags ? { paddingBottom: "5em" } : {}}>

                    {
                        tags.map((t, id) => {
                            return <Tag key={id}>{t}</Tag>
                        })
                    }
                </Tags>
                <Footer>
                    {
                        demo ?
                            <Link href={demo} target="_blank" style={{marginTop:"1em"}}>
                                Visit
                            </Link> : ""
                    }
                    {
                        github ?
                            <Git href={github} target="_blank" style={{marginTop:"1em"}}>
                                <Github width={30} height={30} />
                            </Git> : ""
                    }
                    {
                        knowMore ?
                            <Link href={knowMore} target="_blank" style={{marginTop:"1em"}}>
                                Know More
                            </Link> : ""
                    }
                </Footer>
            </div>


        </Box>
    )
}

export default Card
