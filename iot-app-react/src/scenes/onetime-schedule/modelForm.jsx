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
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import dayjs from "dayjs";

const ModelForm = ({ open, handleClose, handleSubmit, deviceIds, initialData }) => {
  const booleans = [true, false];

  const payload = {
    id: initialData == null ? "" : initialData.id,
    turnOnWater: "",
    duration: "",
    readSensor: "",
    time: null,
    date: null,
    device_id: ""
  }

  const [formData, setFormData] = useState(payload);

  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      const dateTimeString = `${initialData.date} ${initialData.time}`;
      setFormData({
        ...initialData,
        date: initialData.date ? dayjs(initialData.date) : null,
        time: initialData.time ? dayjs(dateTimeString) : null,
      });
    }
  }, [initialData]);

  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle date change
  const handleDateChange = (newValue) => {
    setFormData((prevData) => ({
      ...prevData,
      date: newValue,
    }));
  };

  // Handle time change
  const handleTimeChange = (newValue) => {
    setFormData((prevData) => ({
      ...prevData,
      time: newValue,
    }));
  };

  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();

    // Format date and time for submission
    const submissionData = {
      ...formData,
      date: formData.date ? formData.date.format("YYYY-MM-DD") : null,
      time: formData.time ? formData.time.format("HH:mm:ss") : null,
    };

    handleSubmit(submissionData);
    setFormData(payload);
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
      >
        <DialogTitle>ONETIME SCHEDULE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new onetime schedule to the system.
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
                  label="Device ID"
                  onChange={handleChange}
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
                  <DatePicker
                    label="Date"
                    value={formData.date}
                    onChange={handleDateChange}
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

            {/* Time Picker */}
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