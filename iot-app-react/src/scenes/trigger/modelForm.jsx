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
  FormControl,
  Chip,
  Box
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import Paper from '@mui/material/Paper';

const ModelForm = ({
  open,
  handleClose,
  handleSubmit,
  deviceIds,
  initialData,
  sensors,
  isEdit
}) => {
  const paginationModel = { page: 0, pageSize: 5 };
  const [selectedDeviceIds, setSelectedDeviceIds] = useState([]);
  const [formData, setFormData] = useState({
    id: initialData?.id || "",
    operator: initialData?.operator || "",
    sensor: initialData?.sensor || "",
    value: initialData?.value || "",
    action: initialData?.action || "",
    duration: initialData?.duration || "",
    deviceId: isEdit ? initialData?.deviceId || "" : initialData?.deviceId || []
  });

  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData(prev => ({
        ...prev,
        ...initialData,
        deviceId: isEdit 
          ? initialData.deviceId || ""
          : Array.isArray(initialData.deviceId) 
            ? initialData.deviceId 
            : [initialData.deviceId].filter(Boolean)
      }));
      
      // Set selected rows in DataGrid for create mode
      if (!isEdit && initialData.deviceId) {
        const deviceIdsArray = Array.isArray(initialData.deviceId) 
          ? initialData.deviceId 
          : [initialData.deviceId];
        setSelectedDeviceIds(deviceIdsArray);
      }
    } else {
      setFormData({
        id: "",
        operator: "",
        sensor: "",
        value: "",
        action: "",
        duration: "",
        deviceId: isEdit ? "" : []
      });
      setSelectedDeviceIds([]);
    }
  }, [initialData, isEdit]);

  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle device selection from DataGrid
  const handleDeviceSelection = (newSelection) => {
    if (!isEdit) {
      const selectedDevices = deviceIds
        .filter(device => newSelection.includes(device.deviceId))
        .map(device => device.deviceId);
      
      setFormData(prev => ({
        ...prev,
        deviceId: selectedDevices
      }));
      setSelectedDeviceIds(newSelection);
    }
  };

  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();
    // Ensure deviceId is in the correct format
    const submitData = {
      ...formData,
      deviceId: isEdit 
        ? formData.deviceId || ""
        : Array.isArray(formData.deviceId) 
          ? formData.deviceId 
          : [formData.deviceId].filter(Boolean)
    };
    
    handleSubmit(submitData);
  };

  const columns = [
    { field: "id", headerName: "Device ID" },
    { field: "name", headerName: "Name", flex: 1, cellClassName: "name-column--cell" },
    { field: "controller", headerName: "Controller", flex: 1 },
    { field: "sensors", headerName: "Sensors", flex: 1 }
  ];

  const devicesWithIds = deviceIds.map((row, index) => ({
    id: row.deviceId,
    ...row,
  }));

  const operators = ['=', '>', '<', '>=', '<=', '!='];

  // Get the device IDs to display as chips (always as array)
  const displayDeviceIds = isEdit 
    ? formData.deviceId ? [formData.deviceId] : []
    : Array.isArray(formData.deviceId) 
      ? formData.deviceId 
      : [formData.deviceId].filter(Boolean);

  return (
    <Dialog open={open} onClose={handleClose}>
      <form onSubmit={onSubmit}>
        <DialogTitle>TRIGGER</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to {isEdit ? "edit" : "add"} a trigger.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense" required>
                <InputLabel id="operator-label">Operator</InputLabel>
                <Select
                  labelId="operator-label"
                  id="operator"
                  name="operator"
                  value={formData.operator}
                  onChange={handleChange}
                  label="Operator"
                >
                  {operators.map((operator) => (
                    <MenuItem key={operator} value={operator}>
                      {operator}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense" required>
                <InputLabel id="sensor-label">Sensor</InputLabel>
                <Select
                  labelId="sensor-label"
                  id="sensor"
                  name="sensor"
                  value={formData.sensor}
                  onChange={handleChange}
                  label="Sensor"
                >
                  {sensors.map((sensor) => (
                    <MenuItem key={sensor} value={sensor}>
                      {sensor}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="action"
                name="action"
                label="Action"
                type="text"
                onChange={handleChange}
                value={formData.action}
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
                onChange={handleChange}
                value={formData.duration}
                variant="outlined"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                margin="dense"
                id="value"
                name="value"
                label="Value"
                type="text"
                fullWidth
                onChange={handleChange}
                value={formData.value}
                variant="outlined"
              />
            </Grid>
            
            {/* Show DataGrid only in create mode */}
            {!isEdit && (
              <Grid item xs={12}>
                <Paper sx={{ height: 400, width: '100%' }}>
                  <DataGrid
                    rows={devicesWithIds}
                    columns={columns}
                    initialState={{ pagination: { paginationModel } }}
                    pageSizeOptions={[5, 10]}
                    checkboxSelection
                    sx={{ border: 0 }}
                    onRowSelectionModelChange={handleDeviceSelection}
                    rowSelectionModel={selectedDeviceIds}
                    disableRowSelectionOnClick
                    disableColumnMenu
                  />
                </Paper>
              </Grid>
            )}

            {/* Show device ID dropdown in edit mode */}
            {isEdit && (
              <Grid item xs={12}>
                <FormControl fullWidth variant="outlined" margin="dense" required>
                  <InputLabel id="device-id-label">Device ID</InputLabel>
                  <Select
                    labelId="device-id-label"
                    id="device-id"
                    name="deviceId"
                    value={formData.deviceId}
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
            )}

            {/* Show selected device chips */}
            {displayDeviceIds.length > 0 && (
              <Grid item xs={12}>
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                  {displayDeviceIds.map((deviceId) => (
                    <Chip key={deviceId} label={deviceId} />
                  ))}
                </Box>
              </Grid>
            )}
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} variant="outlined" color="error">Cancel</Button>
          <Button type="submit" variant="outlined" color="primary">Save</Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default ModelForm;