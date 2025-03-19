/** @type {import('tailwindcss').Config} */

const { keyframes, animation } = require('@angular/animations');
const defaultTheme = require('tailwindcss/defaultTheme');
const { transform } = require('typescript');

module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        'black-text' : '#000',
        'yellow-button' : '#FFBD3F',
        'background' : '#080a1b',
        'primary' : '#96a3e8',
        'secondary' : '#162998',
        'accent' : '#203fee',
        'customBorder' : '#424E6F'
      },
      fontFamily: {
        inter: ['Inter', ...defaultTheme.fontFamily.sans],
        domine: ['Domine', defaultTheme.fontFamily.serif],
        heading: ['Kumbh Sans', 'sans-serifs'],
        body: ['Noto Sans Old Hungarian', 'sans-serif']
      },
      fontSize: {
        h1: '4.210rem'
      },
      spacing: {
        customTop: '92px'
      },
      animation: {
        'slideUp-animation': 'slideUp 0.3s ease-in-out'
      },
      keyframes: {
        slideUp: {
          '0%' : {transform: 'translateY(20%)', opacity: '0'},
          '100%' : {transform : 'translateY(0)'}
        }
      }
    },
  },
  plugins: [],
}

