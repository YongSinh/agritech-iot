import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  TextField,
  Grid,
  Button
} from "@mui/material";
import { tokens } from "../../theme";
import { useTheme } from "@emotion/react";
const ModelForm = ({ open, handleClose, handleSubmit }) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={(event) => {
          event.preventDefault(); // Prevent page reload
          const formData = new FormData(event.currentTarget);
          const formJson = Object.fromEntries(formData.entries());
          console.log("Form Data in ModelForm:", formJson);
          handleSubmit(formJson);
        }}
      >
        <DialogTitle>IoT DEVICE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new IoT device to the system.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="device_id"
                name="device_id"
                label="Device ID"
                type="text"
                fullWidth
                variant="standard"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="name"
                name="name"
                label="Full Name"
                type="text"
                fullWidth
                variant="standard"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="controller"
                name="controller"
                label="Controller"
                type="text"
                fullWidth
                variant="standard"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="sensors"
                name="sensors"
                label="Sensors"
                type="text"
                fullWidth
                variant="standard"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                margin="dense"
                id="remark"
                name="remark"
                label="Remark"
                type="text"
                fullWidth
                variant="outlined"
                multiline
                minRows={3}
                maxRows={10}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} variant="outlined" color="error" >Cancel</Button>
          <Button type="submit" variant="outlined" color="primary" >Save</Button>
        </DialogActions>
      </form>
    </Dialog>

  );
};

export default ModelForm;