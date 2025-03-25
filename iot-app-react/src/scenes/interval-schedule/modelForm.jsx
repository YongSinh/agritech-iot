import React, { useEffect, useState } from "react";
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
import dayjs from "dayjs";

const ModelForm = ({ open, handleClose, handleSubmit, deviceIds, initialData }) => {
  const booleans = [true, false];

  const payload = {
    id: initialData == null ? "" : initialData.id,
    device_id: "",
    interval: "",
    turnOnWater: "",
    duration: "",
    readSensor: "",
    runDatetime: null, // Initialize as null or with a default date
  }

  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle date/time change
  const handleDateTimeChange = (newValue) => {
    setFormData((prevData) => ({
      ...prevData,
      runDatetime: newValue,
    }));
  };

  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();
    // Convert Dayjs object to ISO string before submission if needed
    const submissionData = {
      ...formData,
      runDatetime: formData.runDatetime ? formData.runDatetime.format('YYYY-MM-DDTHH:mm:ss') : null,
    };
    handleSubmit(submissionData);
  };
  const [formData, setFormData] = useState(payload)
  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData({
        ...initialData,
        // Convert string date to Dayjs object if needed
        runDatetime: initialData.runDatetime ? dayjs(initialData.runDatetime) : null,
      });
    }
  }, [initialData]);

  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
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
                value={formData.interval}
                onChange={handleChange}
                id="interval"
                name="interval"
                label="Interval"
                type="number"
                fullWidth
                variant="outlined"
              />
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
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <FormControl fullWidth variant="outlined" margin="dense">
                  <DateTimePicker
                    label="Run DateTime"
                    value={formData.runDatetime}
                    onChange={handleDateTimeChange}
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