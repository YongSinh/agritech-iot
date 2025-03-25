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
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';

const ModelForm = ({ open, handleClose, handleSubmit, deviceIds }) => {
  const [deviceId, setDeviceId] = useState('');
  const booleans = [true, false];
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
        <DialogTitle>INTERVAL SCHEDULE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new interval schedule to the system.
          </DialogContentText>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="operator"
                name="interval"
                label="Interval"
                type="text"
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="demo-simple-select-label">Turn on water</InputLabel>
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  label="Turn on water"
                  fullWidth
                >

                  {
                    booleans.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value.toString()}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="read-sensor-label">Read Sensor</InputLabel>
                <Select
                  labelId="read-sensor-label"
                  fullWidth
                  label="Read Sensor"
                >
                  {
                    booleans.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value.toString()}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <FormControl fullWidth variant="outlined" margin="dense">
                  <DateTimePicker label="Run DateTime" sx={{ width: '100%' }} />
                </FormControl>
              </LocalizationProvider>
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