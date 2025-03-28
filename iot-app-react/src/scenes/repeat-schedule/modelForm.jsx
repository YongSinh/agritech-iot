import React, { useState, useEffect } from "react";
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
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import dayjs from "dayjs";
const ModelForm = ({ open, handleClose, handleSubmit, deviceIds, initialData }) => {
  const booleans = [true, false];
  const payload = {
    id: initialData == null ? "" : initialData.id,
    turnOnWater: "",
    duration: "",
    readSensor: "",
    time: null,
    day: "",
    device_id: ""
  }

  const [formData, setFormData] = useState(payload);

  // Modified handleChange to handle day case conversion
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: name === 'day' ? value.toUpperCase() : value,
    }));
  };

  const handleTimeChange = (newValue) => {
    setFormData((prevData) => ({
      ...prevData,
      time: newValue,
    }));
  };



  const daysOfWeek = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];


  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();

    // Format date and time for submission
    const submissionData = {
      ...formData,
      time: formData.time ? formData.time.format("HH:mm:ss") : null,
    };

    handleSubmit(submissionData);
    setFormData(payload);
  };

  // Normalize the day value to title case for display
  const normalizeDay = (day) => {
    if (!day) return '';
    return day.charAt(0).toUpperCase() + day.slice(1).toLowerCase();
  };

  useEffect(() => {
    if (initialData) {
      const today = dayjs().format('YYYY-MM-DD');
      setFormData({
        ...initialData,
        day: initialData.day ? initialData.day.toUpperCase() : '',
        time: initialData.time ? dayjs(`${today} ${initialData.time}`) : null,
      });
    }
  }, [initialData]);

  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
      >
        <DialogTitle>REPEAT SCHEDULE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new repeat schedule to the system.
          </DialogContentText>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense">
                <InputLabel id="device-id-label">Device ID</InputLabel>
                <Select
                  labelId="device-id-label"
                  id="device-id"
                  name="device_id"
                  value={formData.device_id}
                  onChange={handleChange}
                  label="Device ID"
                  fullWidth
                >
                  {deviceIds.map((device, index) => (
                    <MenuItem key={index} value={device.deviceId}>
                      {device.deviceId}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="day-select-label">Days of week</InputLabel>
                <Select
                  labelId="day-select-label"
                  id="day-select"
                  name="day"
                  value={formData.day}  // Store as uppercase internally
                  onChange={handleChange}
                  label="Days of week"
                  fullWidth
                >
                  {daysOfWeek.map((value, index) => (
                    <MenuItem 
                      key={index} 
                      value={value.toUpperCase()}  // Store value as uppercase
                    >
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="turn-on-water-select-label">Turn on water</InputLabel>
                <Select
                  labelId="turn-on-water-select-label"
                  id="turn-on-water"
                  label="Turn on water"
                  name="turnOnWater"
                  onChange={handleChange}
                  value={formData.turnOnWater}
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
                  id="read-sensor"
                  fullWidth
                  name="readSensor"
                  onChange={handleChange}
                  value={formData.readSensor}
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
              <TextField
                required
                margin="dense"
                id="duration"
                name="duration"
                label="Duration"
                type="number"
                onChange={handleChange}
                value={formData.duration}
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <FormControl fullWidth margin="dense">
                  <TimePicker
                    label="Time"
                    value={formData.time}
                    onChange={handleTimeChange}
                    sx={{ width: '100%' }}
                    slotProps={{
                      textField: {
                        variant: 'outlined',
                      },
                    }}
                  />
                </FormControl>
              </LocalizationProvider>
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