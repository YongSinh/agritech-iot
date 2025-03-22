import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  TextField,
  Grid,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl
} from "@mui/material";
const ModelForm = ({ open, handleClose, handleSubmit, deviceIds }) => {
  const [deviceId, setDeviceId] = useState('');
  const handleChange = (event) => {
    setDeviceId(event.target.value);
  };

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
        <DialogTitle>TRIGGER</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new trigger to the system.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="operator"
                name="operator"
                label="Operator"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="sensor"
                name="sensor"
                label="Sensor"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="value"
                name="value"
                label="Value"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="action"
                name="action"
                label="Action"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="duration"
                name="duration"
                label="Duration"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense">
                <InputLabel id="device-id-label">Device ID</InputLabel>
                <Select
                  labelId="device-id-label"
                  id="device-id"
                  value={deviceId}
                  onChange={handleChange}
                >
                  {deviceIds.map((device) => (
                    <MenuItem key={device.deviceId} value={device.deviceId}>
                      {device.deviceId}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
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