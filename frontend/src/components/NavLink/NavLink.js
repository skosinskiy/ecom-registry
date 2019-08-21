import {NavLink} from "react-router-dom";
import React from "react";
import withStyles from "@material-ui/core/styles/withStyles";

const styles = theme => ({
    link: {
        color: theme.palette.grey.A700,
        textDecoration: 'none'
    }
})

export const navLink = props => {

    const {to, children, classes} = props

    return (
        <NavLink to={to} className={classes.link}>
            {children}
        </NavLink>
    )
}

export default withStyles(styles)(navLink)