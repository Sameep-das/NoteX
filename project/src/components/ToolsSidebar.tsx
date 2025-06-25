import React, { useState } from 'react';
import { 
  Pen, 
  Square, 
  Circle, 
  Triangle, 
  Type, 
  Image, 
  Eraser, 
  Palette, 
  Download, 
  FileText, 
  Grid3X3, 
  Layers,
  Brush,
  PenTool,
  Move,
  RotateCcw,
  Trash2,
  Plus,
  Search,
  Filter,
  MoreVertical,
  Star,
  Eye,
  Lock
} from 'lucide-react';

const ToolsSidebar: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'tools' | 'gallery'>('tools');
  const [selectedTool, setSelectedTool] = useState('pen');
  const [selectedColor, setSelectedColor] = useState('#3B82F6');

  const drawingTools = [
    { id: 'pen', icon: Pen, label: 'Pen' },
    { id: 'brush', icon: Brush, label: 'Brush' },
    { id: 'pencil', icon: PenTool, label: 'Pencil' },
    { id: 'eraser', icon: Eraser, label: 'Eraser' },
  ];

  const shapeTools = [
    { id: 'rectangle', icon: Square, label: 'Rectangle' },
    { id: 'circle', icon: Circle, label: 'Circle' },
    { id: 'triangle', icon: Triangle, label: 'Triangle' },
    { id: 'line', icon: Move, label: 'Line' },
  ];

  const colors = [
    '#3B82F6', '#EF4444', '#10B981', '#F59E0B', 
    '#8B5CF6', '#F97316', '#06B6D4', '#84CC16',
    '#EC4899', '#6B7280', '#000000', '#FFFFFF'
  ];

  const mockNotes = [
    { 
      id: 1, 
      title: 'Chemistry Notes', 
      thumbnail: 'https://images.pexels.com/photos/2280549/pexels-photo-2280549.jpeg?auto=compress&cs=tinysrgb&w=300',
      type: 'public',
      likes: 24,
      views: 156
    },
    { 
      id: 2, 
      title: 'Math Formulas', 
      thumbnail: 'https://images.pexels.com/photos/6238040/pexels-photo-6238040.jpeg?auto=compress&cs=tinysrgb&w=300',
      type: 'group',
      likes: 18,
      views: 89
    },
    { 
      id: 3, 
      title: 'Physics Concepts', 
      thumbnail: 'https://images.pexels.com/photos/8197543/pexels-photo-8197543.jpeg?auto=compress&cs=tinysrgb&w=300',
      type: 'private',
      likes: 0,
      views: 23
    },
    { 
      id: 4, 
      title: 'Biology Diagrams', 
      thumbnail: 'https://images.pexels.com/photos/8166849/pexels-photo-8166849.jpeg?auto=compress&cs=tinysrgb&w=300',
      type: 'draft',
      likes: 0,
      views: 5
    },
  ];

  const templates = [
    { id: 1, name: 'Cornell Notes', thumbnail: 'https://images.pexels.com/photos/6238040/pexels-photo-6238040.jpeg?auto=compress&cs=tinysrgb&w=300' },
    { id: 2, name: 'Mind Map', thumbnail: 'https://images.pexels.com/photos/8197543/pexels-photo-8197543.jpeg?auto=compress&cs=tinysrgb&w=300' },
    { id: 3, name: 'Graph Paper', thumbnail: 'https://images.pexels.com/photos/2280549/pexels-photo-2280549.jpeg?auto=compress&cs=tinysrgb&w=300' },
    { id: 4, name: 'Flowchart', thumbnail: 'https://images.pexels.com/photos/8166849/pexels-photo-8166849.jpeg?auto=compress&cs=tinysrgb&w=300' },
  ];

  return (
    <div className="w-80 bg-white dark:bg-gray-900 border-l border-gray-200 dark:border-gray-700 h-full flex flex-col transition-colors duration-200">
      {/* Tab Navigation */}
      <div className="p-4 border-b border-gray-200 dark:border-gray-700">
        <div className="flex bg-gray-100 dark:bg-gray-800 rounded-lg p-1">
          <button
            onClick={() => setActiveTab('tools')}
            className={`flex-1 py-2 px-4 rounded-md text-sm font-medium transition-colors duration-200 ${
              activeTab === 'tools'
                ? 'bg-white dark:bg-gray-700 text-blue-600 dark:text-blue-400 shadow-sm'
                : 'text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white'
            }`}
          >
            Drawing Tools
          </button>
          <button
            onClick={() => setActiveTab('gallery')}
            className={`flex-1 py-2 px-4 rounded-md text-sm font-medium transition-colors duration-200 ${
              activeTab === 'gallery'
                ? 'bg-white dark:bg-gray-700 text-blue-600 dark:text-blue-400 shadow-sm'
                : 'text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white'
            }`}
          >
            Notes Gallery
          </button>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto">
        {activeTab === 'tools' ? (
          <div className="p-4 space-y-6">
            {/* Drawing Tools */}
            <div>
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-3">Drawing Tools</h3>
              <div className="grid grid-cols-2 gap-2">
                {drawingTools.map((tool) => (
                  <ToolItem
                    key={tool.id}
                    icon={tool.icon}
                    label={tool.label}
                    active={selectedTool === tool.id}
                    onClick={() => setSelectedTool(tool.id)}
                  />
                ))}
              </div>
            </div>

            {/* Shape Tools */}
            <div>
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-3">Shapes</h3>
              <div className="grid grid-cols-2 gap-2">
                {shapeTools.map((tool) => (
                  <ToolItem
                    key={tool.id}
                    icon={tool.icon}
                    label={tool.label}
                    active={selectedTool === tool.id}
                    onClick={() => setSelectedTool(tool.id)}
                  />
                ))}
              </div>
            </div>

            {/* Color Palette */}
            <div>
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-3">Colors</h3>
              <div className="grid grid-cols-4 gap-2">
                {colors.map((color) => (
                  <button
                    key={color}
                    onClick={() => setSelectedColor(color)}
                    className={`w-8 h-8 rounded-lg border-2 transition-all duration-200 ${
                      selectedColor === color
                        ? 'border-gray-400 dark:border-gray-500 scale-110'
                        : 'border-gray-200 dark:border-gray-700 hover:scale-105'
                    }`}
                    style={{ backgroundColor: color }}
                  />
                ))}
              </div>
            </div>

            {/* Additional Tools */}
            <div>
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-3">Other Tools</h3>
              <div className="space-y-2">
                <button className="w-full flex items-center space-x-3 p-3 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200">
                  <Type className="h-5 w-5" />
                  <span>Text</span>
                </button>
                <button className="w-full flex items-center space-x-3 p-3 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200">
                  <Image className="h-5 w-5" />
                  <span>Image</span>
                </button>
                <button className="w-full flex items-center space-x-3 p-3 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200">
                  <Grid3X3 className="h-5 w-5" />
                  <span>Grid</span>
                </button>
                <button className="w-full flex items-center space-x-3 p-3 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200">
                  <Layers className="h-5 w-5" />
                  <span>Layers</span>
                </button>
              </div>
            </div>

            {/* Templates */}
            <div>
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white mb-3">Templates</h3>
              <div className="grid grid-cols-2 gap-2">
                {templates.map((template) => (
                  <div
                    key={template.id}
                    className="relative group cursor-pointer rounded-lg overflow-hidden bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors duration-200"
                  >
                    <img
                      src={template.thumbnail}
                      alt={template.name}
                      className="w-full h-20 object-cover"
                    />
                    <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-40 transition-all duration-200 flex items-center justify-center">
                      <Plus className="h-6 w-6 text-white opacity-0 group-hover:opacity-100 transition-opacity duration-200" />
                    </div>
                    <div className="p-2">
                      <p className="text-xs font-medium text-gray-900 dark:text-white truncate">
                        {template.name}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        ) : (
          <div className="p-4 space-y-4">
            {/* Search and Filter */}
            <div className="space-y-3">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search notes..."
                  className="w-full pl-10 pr-4 py-2 border border-gray-200 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors duration-200"
                />
              </div>
              <div className="flex items-center space-x-2">
                <button className="flex items-center space-x-2 px-3 py-1.5 bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 rounded-lg text-sm text-gray-700 dark:text-gray-300 transition-colors duration-200">
                  <Filter className="h-4 w-4" />
                  <span>Filter</span>
                </button>
                <select className="px-3 py-1.5 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg text-sm text-gray-700 dark:text-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors duration-200">
                  <option>All Notes</option>
                  <option>Public</option>
                  <option>Group</option>
                  <option>Private</option>
                  <option>Drafts</option>
                </select>
              </div>
            </div>

            {/* Notes Grid */}
            <div className="space-y-3">
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white">Your Notes</h3>
              <div className="space-y-3">
                {mockNotes.map((note) => (
                  <NoteCard key={note.id} note={note} />
                ))}
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Action Buttons */}
      <div className="p-4 border-t border-gray-200 dark:border-gray-700">
        <div className="space-y-2">
          <button className="w-full flex items-center justify-center space-x-2 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors duration-200 font-medium">
            <Plus className="h-4 w-4" />
            <span>New Canvas</span>
          </button>
          <button className="w-full flex items-center justify-center space-x-2 py-2 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200 font-medium">
            <Download className="h-4 w-4" />
            <span>Export</span>
          </button>
        </div>
      </div>
    </div>
  );
};

interface ToolItemProps {
  icon: React.ComponentType<any>;
  label: string;
  active: boolean;
  onClick: () => void;
}

const ToolItem: React.FC<ToolItemProps> = ({ icon: Icon, label, active, onClick }) => {
  return (
    <button
      onClick={onClick}
      className={`flex flex-col items-center space-y-1 p-3 rounded-lg transition-colors duration-200 ${
        active
          ? 'bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400'
          : 'text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800 hover:text-gray-900 dark:hover:text-white'
      }`}
    >
      <Icon className="h-5 w-5" />
      <span className="text-xs font-medium">{label}</span>
    </button>
  );
};

interface NoteCardProps {
  note: {
    id: number;
    title: string;
    thumbnail: string;
    type: string;
    likes: number;
    views: number;
  };
}

const NoteCard: React.FC<NoteCardProps> = ({ note }) => {
  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'public':
        return <Eye className="h-3 w-3" />;
      case 'private':
        return <Lock className="h-3 w-3" />;
      case 'group':
        return <FileText className="h-3 w-3" />;
      default:
        return <FileText className="h-3 w-3" />;
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'public':
        return 'text-green-600 dark:text-green-400 bg-green-100 dark:bg-green-900/30';
      case 'private':
        return 'text-red-600 dark:text-red-400 bg-red-100 dark:bg-red-900/30';
      case 'group':
        return 'text-blue-600 dark:text-blue-400 bg-blue-100 dark:bg-blue-900/30';
      default:
        return 'text-gray-600 dark:text-gray-400 bg-gray-100 dark:bg-gray-800';
    }
  };

  return (
    <div className="group relative bg-gray-50 dark:bg-gray-800 rounded-lg overflow-hidden hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors duration-200 cursor-pointer">
      <div className="relative">
        <img
          src={note.thumbnail}
          alt={note.title}
          className="w-full h-32 object-cover"
        />
        <div className="absolute top-2 right-2">
          <button className="p-1 bg-white dark:bg-gray-800 rounded-full shadow-sm opacity-0 group-hover:opacity-100 transition-opacity duration-200">
            <MoreVertical className="h-4 w-4 text-gray-600 dark:text-gray-400" />
          </button>
        </div>
        <div className="absolute bottom-2 left-2">
          <div className={`flex items-center space-x-1 px-2 py-1 rounded-full text-xs font-medium ${getTypeColor(note.type)}`}>
            {getTypeIcon(note.type)}
            <span className="capitalize">{note.type}</span>
          </div>
        </div>
      </div>
      <div className="p-3">
        <h4 className="font-medium text-gray-900 dark:text-white truncate mb-2">
          {note.title}
        </h4>
        <div className="flex items-center justify-between text-xs text-gray-500 dark:text-gray-400">
          <div className="flex items-center space-x-3">
            <div className="flex items-center space-x-1">
              <Star className="h-3 w-3" />
              <span>{note.likes}</span>
            </div>
            <div className="flex items-center space-x-1">
              <Eye className="h-3 w-3" />
              <span>{note.views}</span>
            </div>
          </div>
          <div className="text-xs">
            2 days ago
          </div>
        </div>
      </div>
    </div>
  );
};

export default ToolsSidebar;